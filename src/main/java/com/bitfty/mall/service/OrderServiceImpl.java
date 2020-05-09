package com.bitfty.mall.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bitfty.enums.OperType;
import com.bitfty.eventBus.EventBusDispatcher;
import com.bitfty.mall.bridge.OrderBridgeServiceImpl;
import com.bitfty.mall.dao.BuySnapshotDao;
import com.bitfty.mall.dao.OrderDao;
import com.bitfty.mall.dao.ProductDao;
import com.bitfty.mall.dto.*;
import com.bitfty.mall.entity.Order;
import com.bitfty.mall.entity.Product;
import com.bitfty.mall.entity.BuySnapshot;
import com.bitfty.mall.enums.OrderStatus;
import com.bitfty.mall.eventbus.event.OrderEvent;
import com.bitfty.mall.exception.ServiceCode;
import com.bitfty.mall.exception.ServiceException;
import com.bitfty.user.dto.BillSerialDto;
import com.bitfty.user.dto.BillSerialPaymentDto;
import com.bitfty.user.enums.BusinessTypeEnum;
import com.bitfty.user.service.BillSerialService;
import com.bitfty.util.BeanMapper;
import com.bitfty.util.CurrencyType;
import com.bitfty.util.Page;
import com.bitfty.util.SnowflakeIdWorker;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service(timeout = 5000)
public class OrderServiceImpl implements OrderService {

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private static final String USD_EXCHANGE_RATE_FOR_CURRENCY = "USD_EXCHANGE_RATE_FOR_CURRENCY";

    private static final Long ONE_DAY = 86400000L;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OrderBridgeServiceImpl orderBridgeService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private BuySnapshotDao buySnapshotDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private EventBusDispatcher eventBusDispatcher;

    @Autowired
    private CoinServiceImpl coinService;

    @Reference
    private BillSerialService billSerialService;


    @Override
    public Map<String,Integer> querySystemIndexReport() {
        Map<String, Integer> resultMap = Maps.newHashMap();
            Map<String, Object> paramsParam = Maps.newHashMap();
            paramsParam.put("status", 1);
            Integer waitPay = orderDao.countByStatus(paramsParam);
            resultMap.put("waitPay",waitPay==null?0:waitPay);

            paramsParam.put("status", 2);
            Integer rechargeStatus = orderDao.countByParams(paramsParam);
            resultMap.put("rechargeStatus",rechargeStatus==null?0:rechargeStatus);
        return resultMap;
    }

    @Override
    public OrderCreateReturnDto create(Long uid, String productId, Integer count, Integer days) {

        Product product = productDao.queryById(productId);
        if (product == null) {
            throw new ServiceException(ServiceCode.PRODUCT_NOT_EXIST);
        }

        Order order = initOrder(product, uid, count, days);
        BuySnapshot buySnapshot = initBuySnapshot(order, product, uid, count, days);

        try {
            orderBridgeService.orderCreateRollBack(order ,buySnapshot, productId, count);
        } catch (Exception e) {
            logger.info("order creation failed order={},buySnapshot={},e={}", order.toString(),buySnapshot.toString(),e);
            throw new ServiceException(ServiceCode.ORDER_CREATE_FAIL);
        }

        OrderCreateReturnDto orderCreateReturnDto = initOrderCreateReturnDto(order);
        return orderCreateReturnDto;
    }

    @Override
    public Page<OrderListDto> page(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        params.put("ends", pageSize);
        params.put("start", (pageNo - 1) * pageSize);
        List<OrderListDto> orders = orderDao.findPageByParams(params);

        List<BillSerialPaymentDto> billSerialPaymentDtos = billSerialService.queryBillSerialByOperType(OperType.PURCHASE.getType(), BusinessTypeEnum.ORDER_PAY_BUSINESS.getType());
        Map<String, BillSerialPaymentDto> bspdm = billSerialPaymentDtos.stream().collect(Collectors.toMap(BillSerialPaymentDto::getBusinessId, Function.identity()));

        for(OrderListDto orderDto:orders){
            orderDto.setFirstPeriodDay(orderDto.getDays());
            if(orderDto.getStatus() != OrderStatus.ORDER_PAY_SUCCESS.getStatus()){
                continue;
            }
            BillSerialPaymentDto billSerialPaymentDto = bspdm.get(orderDto.getId());
            if(billSerialPaymentDto == null){
                logger.info("order bill_serial not exist orderDto={}", orderDto);
                continue;
            }
            orderDto.setPayType(billSerialPaymentDto.getCurrencyType());
            JSONObject exchangeJson = JSONObject.parseObject(orderDto.getExchangeRate());
            JSONObject amountJson = exchangeJson.getJSONObject(orderDto.getCurrencyType());
            orderDto.setPayAmount(new BigDecimal(amountJson.get("amount").toString()).stripTrailingZeros().toPlainString());
            orderDto.setElectricFee(orderDto.getElectricFee());
            if (orderDto.getStatus() == 3 && CurrencyType.BTC.getType().equals(orderDto.getCurrencyType())) {
                //获取汇率
                Map<String,JSONObject>  maps = JSON.parseObject(orderDto.getExchangeRate(),Map.class);
                JSONObject btc = maps.get(orderDto.getCurrencyType());
                BigDecimal rate = btc.getBigDecimal("proportion");
                orderDto.setElectricFee(orderDto.getElectricFee().divide(rate,8, RoundingMode.DOWN));
            }
        }

        Integer total = orderDao.countByParams(params);
        return new Page(total, pageSize, pageNo, orders);
    }

    @Override
    public Boolean cancel(String id, Long uid) {

        List<BuySnapshot> buySnapshots = buySnapshotDao.queryBuySnapshotByOrderId(id);
        if (CollectionUtils.isEmpty(buySnapshots)) {
            throw new ServiceException(ServiceCode.BUYSNAPSHOT_NOT_EXIST);
        }
        try {
            for (BuySnapshot buySnapshot:buySnapshots) {
                orderBridgeService.orderCancelRollBack(id, uid, buySnapshot.getProdId());
            }
        } catch (Exception e) {
            logger.info("取消订单异常,id{},uid{}", id, uid, e);
            throw new ServiceException(ServiceCode.ORDER_CANCEL_FAIL);
        }
        return true;
    }

    @Override
    public OrderDto queryByUid(Long uid) {
        Order order = orderDao.queryByUid(uid);
        return BeanMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto queryByIdAndUid(Long uid,String id) {
        Order order = orderDao.queryByIdAndUid(uid,id);
        return BeanMapper.map(order, OrderDto.class);
    }

    @Override
    public List<Map<String,Object>> queryOrderIds() {
        return orderDao.queryOrderIds();
    }

	@Override
	public List<String> findInHandleOrderByIds(List<String> orderIdList) {
		return orderDao.findInHandleOrderByIds(orderIdList);
	}

    @Override
    public List<OrderDto> findOrderByIds(List<String> orderIds) {
        List<Order> orderList = orderDao.findOrderByIds(orderIds);
        return BeanMapper.mapList(orderList, OrderDto.class);
    }

    @Override
    public void update(OrderDto orderDto) {
        Order order = orderDao.queryById(orderDto.getId());
        if(order == null){
            throw new ServiceException(ServiceCode.FAILD, "没有找到订单");
        }
        if (order.getStatus() != OrderStatus.ORDER_PAY_WAIT.getStatus()) {
            throw new ServiceException(ServiceCode.FAILD, "不是待支付订单");
        }
        order.setAmount(orderDto.getAmount());
        order.setExchangeRate(getExchangeRate(orderDto.getAmount()));
        orderDao.update(order);
    }

    @Override
    public  Page<OrderBackstageDto> pageBackstage(Integer pageNo, Integer pageSize, Map<String, Object> params) {
        params.put("start", (pageNo - 1) * pageSize);
        params.put("ends", pageSize);

        List<Order> orders = orderDao.findPageBackstageByParams(params);
        List<BillSerialPaymentDto> billSerialPaymentDtos = billSerialService.queryBillSerialByOperType(OperType.PURCHASE.getType(), BusinessTypeEnum.ORDER_PAY_BUSINESS.getType());

        Map<String, BillSerialPaymentDto> billSerialPaymentDtoMaps = billSerialPaymentDtos.stream().collect(Collectors.toMap(BillSerialPaymentDto::getBusinessId, Function.identity()));
        List<OrderBackstageDto> orderBackstageDto = Lists.newArrayList();
        for (Order order : orders) {
            OrderBackstageDto obd = new OrderBackstageDto();
            BeanMapper.copy(order, obd);
            if (OrderStatus.ORDER_PAY_SUCCESS.getStatus() != order.getStatus()) {
                orderBackstageDto.add(obd);
                continue;
            }
            String currencyType = Optional.ofNullable(billSerialPaymentDtoMaps.get(order.getId())).map(i -> i.getCurrencyType()).orElse(null);
            obd.setCurrencyType(currencyType);
            orderBackstageDto.add(obd);
        }
        Integer total = orderDao.countPageBackstageByParams(params);
        return new Page(total, pageSize, pageNo, orderBackstageDto);
    }

    @Override
    public OrderDetailDto queryOrderDetail(Long uid, String id) {

        Order order = orderDao.queryByIdAndUid(uid, id);
        if (order == null) {
            throw new ServiceException(ServiceCode.FAILD, "订单不存在");
        }
        List<BuySnapshot> buySnapshots = buySnapshotDao.queryBuySnapshotByOrderId(id);
        if(CollectionUtils.isEmpty(buySnapshots)){
            throw new ServiceException(ServiceCode.FAILD,"快照不存在");
        }
        BillSerialDto billSerialDto = null;
        if(OrderStatus.ORDER_PAY_SUCCESS.getStatus() == order.getStatus()){
            billSerialDto = billSerialService.queryBillSerialByBusinessId(id, BusinessTypeEnum.ORDER_PAY_BUSINESS.getType());
        }
        OrderDetailDto orderDetailDto = initOrderDetailDto(order, buySnapshots.get(0), billSerialDto);
        return orderDetailDto;
    }

    public OrderDto queryById(String orderId) {
        Order order = orderDao.queryById(orderId);
        if (order == null) {
            return null;
        }
        return BeanMapper.map(order,OrderDto.class);
    }

    @Override
    public List<String> findByStatusAndTime(int status, Date startTime) {
        return orderDao.findIdByStatusAndTime(status,startTime);
    }

    @Override
    public Boolean cancelOrderForJob(String id, Long uid, String prodId) {
        try {
            orderBridgeService.orderCancelRollBack(id, uid, prodId);
        } catch (Exception e) {
            logger.info("取消订单异常,id{}", id, e);
            throw new ServiceException(ServiceCode.ORDER_CANCEL_FAIL);
        }
        return true;
    }

    @Override
    public void finishOrderStatus(String orderId,String currencyType) {
        Integer result = orderDao.finishOrderStatus(orderId,currencyType);
        if(result <= 0 ) {
            logger.info("repeated modification");
            return;
        }
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrderId(orderId);
        orderEvent.setStatus(OrderStatus.ORDER_PAY_SUCCESS.getStatus());
        orderEvent.setCreateAt(new Date());
        eventBusDispatcher.dispatcher(orderEvent);
    }


    @Override
    public List<String> findByStatus(int status) {
       return orderDao.findByStatus(status);
    }

    public OrderDto findByIdStatus( String orderId, int status, Long uid){
        //支付查订单，强制查询主库
        Order order = orderDao.findByIdStatus(orderId, status, uid);
        return BeanMapper.map(order,OrderDto.class);
    }

    private BuySnapshot initBuySnapshot(Order order, Product product, Long uid, Integer count, Integer days) {
        BuySnapshot buySnapshot = new BuySnapshot();
        buySnapshot.setId(DigestUtils.md5Hex(String.valueOf(uid) + System.currentTimeMillis()));
        buySnapshot.setOrderId(order.getId());
        buySnapshot.setProdId(product.getId());
        buySnapshot.setPictureUrl(product.getPictureUrl());
        buySnapshot.setName(product.getName());
        buySnapshot.setAmount(product.getAmount());
        buySnapshot.setSoldAmount(product.getSoldAmount());
        buySnapshot.setElectricFee(product.getElectricFee());
        buySnapshot.setDays(days);
        buySnapshot.setSoldElectricFee(product.getSoldElectricFee());
        buySnapshot.setServiceFee(product.getServiceFee());
        buySnapshot.setPowerSize(new BigDecimal("1"));
        if (product.getProdType().equals("2") || product.getProdType().equals("3") ) {
            buySnapshot.setPowerSize(product.getPowerSize());
        }
        buySnapshot.setUserId(String.valueOf(uid));
        buySnapshot.setCurrencyType(product.getCurrencyType());
        buySnapshot.setProdType(product.getProdType());
        buySnapshot.setGainDate(product.getGainDate());
        buySnapshot.setValidDay(product.getValidDay());
        buySnapshot.setBuyCount(count);
        if (System.currentTimeMillis() > product.getGainDate().getTime()) {
            buySnapshot.setExpireDate(new Date(getSecondDayTime() + product.getValidDay() * ONE_DAY));
        } else {
            buySnapshot.setExpireDate(new Date(product.getGainDate().getTime() + product.getValidDay() * ONE_DAY));
        }
        buySnapshot.setMemo(product.getMemo());
        buySnapshot.setCreateAt(new Date());
        buySnapshot.setUpdateAt(new Date());
        return buySnapshot;
    }

    private Order initOrder(Product product, Long uid, Integer count, Integer days) {
        Order order = new Order();
        order.setId("D" + SnowflakeIdWorker.getId());
        order.setUserId(uid);
        if (product.getProdType().equals("1")) {
            order.setElectricFee(product.getSoldElectricFee().multiply(BigDecimal.valueOf(count)).multiply(BigDecimal.valueOf(days)));
            order.setProdAmount(product.getSoldAmount().multiply(BigDecimal.valueOf(product.getValidDay())).multiply(BigDecimal.valueOf(count)));
        }
        if (product.getProdType().equals("2") || product.getProdType().equals("3")) {
            order.setElectricFee(product.getSoldElectricFee().multiply(BigDecimal.valueOf(count)).multiply(BigDecimal.valueOf(days)).multiply(product.getPowerSize()));
            order.setProdAmount(product.getSoldAmount().multiply(BigDecimal.valueOf(count)));
        }
        order.setAmount(order.getProdAmount().add(order.getElectricFee()));
        order.setExchangeRate(getExchangeRate(order.getAmount()));
        order.setStatus(OrderStatus.ORDER_INIT.getStatus());
        order.setCreateAt(new Date());
        order.setUpdateAt(new Date());
        return order;
    }

    private String getExchangeRate(BigDecimal amount) {
        Map<String, CurrencyExchangeRateDto> mapList = Maps.newHashMap();

        //支持usdt
        CurrencyExchangeRateDto usdtExchangeRate = new CurrencyExchangeRateDto();
        usdtExchangeRate.setAmount(amount);
        usdtExchangeRate.setProportion(BigDecimal.valueOf(1));
        mapList.put("USDT", usdtExchangeRate);

        String rate = redisTemplate.opsForValue().get(USD_EXCHANGE_RATE_FOR_CURRENCY);
        //没有获取到币价，就取一次
        if (StringUtils.isBlank(rate)) {
            BigDecimal btcToDollar = coinService.getBtcToDollar();
            //获取当前币价失败，只支持usdt
            if (btcToDollar == null) {
                return JSONUtil.toJsonStr(mapList);
            }
            CurrencyExchangeRateDto btcExchangeRate = new CurrencyExchangeRateDto();
            btcExchangeRate.setAmount(amount.divide(btcToDollar, 8, BigDecimal.ROUND_DOWN));
            btcExchangeRate.setProportion(btcToDollar);
            mapList.put("BTC", btcExchangeRate);
            return JSONUtil.toJsonStr(mapList);
        }

        if(StringUtils.isNotBlank(rate)){
            Map<String,BigDecimal> currencyExchangeRates = JSON.parseObject(rate,Map.class);
            for (Map.Entry<String, BigDecimal> m : currencyExchangeRates.entrySet()) {
                CurrencyExchangeRateDto currencyExchangeRate = new CurrencyExchangeRateDto();
                currencyExchangeRate.setProportion(m.getValue());
                currencyExchangeRate.setAmount(amount.divide(m.getValue(), 8, BigDecimal.ROUND_DOWN));
                mapList.put(m.getKey(), currencyExchangeRate);
            }
        }

        return JSONUtil.toJsonStr(mapList);
    }

    private OrderCreateReturnDto initOrderCreateReturnDto(Order order) {
        OrderCreateReturnDto orderCreateReturnDto = new OrderCreateReturnDto();
        orderCreateReturnDto.setId(order.getId());
        orderCreateReturnDto.setStatus(order.getStatus());
        return orderCreateReturnDto;
    }

    private OrderDetailDto initOrderDetailDto(Order order, BuySnapshot buySnapshot, BillSerialDto billSerialDto) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setId(order.getId());
        orderDetailDto.setCreateAt(order.getCreateAt());
        orderDetailDto.setUid(order.getUserId());
        if (billSerialDto != null) {
            orderDetailDto.setPayment(billSerialDto.getCurrencyType());
        }
        orderDetailDto.setStatus(order.getStatus());
        orderDetailDto.setProductType(Integer.valueOf(buySnapshot.getProdType()));
        orderDetailDto.setPictureUrl(buySnapshot.getPictureUrl());
        orderDetailDto.setName(buySnapshot.getName());
        orderDetailDto.setCurrencyType(order.getCurrencyType());
        orderDetailDto.setPowerSize(buySnapshot.getPowerSize().stripTrailingZeros().toPlainString());
        orderDetailDto.setValidDay(buySnapshot.getValidDay());
        orderDetailDto.setSoldAmount(buySnapshot.getSoldAmount().stripTrailingZeros().toPlainString());
        orderDetailDto.setBuyCount(buySnapshot.getBuyCount());
        orderDetailDto.setElectricDays(buySnapshot.getDays());
        orderDetailDto.setSoldElectricFee(buySnapshot.getSoldElectricFee().stripTrailingZeros().toPlainString());
        orderDetailDto.setElectricFee(order.getElectricFee().stripTrailingZeros().toPlainString());

        orderDetailDto.setOriginalAmount(order.getAmount().add(order.getCouponAmount()).stripTrailingZeros().toPlainString());
        orderDetailDto.setCouponAmount(order.getCouponAmount().stripTrailingZeros().toPlainString());
        orderDetailDto.setAmount(order.getAmount().stripTrailingZeros().toPlainString());
        orderDetailDto.setProdAmount(order.getProdAmount().stripTrailingZeros().toPlainString());
        orderDetailDto.setProdId(buySnapshot.getProdId());
        orderDetailDto.setExchangeRate((Map) JSON.parse(order.getExchangeRate()));
        return orderDetailDto;
    }

    private Long getSecondDayTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime().getTime();
    }


}
