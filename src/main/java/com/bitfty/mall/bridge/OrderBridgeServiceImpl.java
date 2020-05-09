package com.bitfty.mall.bridge;

import com.bitfty.mall.dao.OrderDao;
import com.bitfty.mall.dao.BuySnapshotDao;
import com.bitfty.mall.entity.Order;
import com.bitfty.mall.entity.BuySnapshot;
import com.bitfty.mall.enums.OrderStatus;
import com.bitfty.mall.exception.ServiceCode;
import com.bitfty.mall.exception.ServiceException;
import com.bitfty.mall.service.ProductService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @ClassName: OrderBridgeServiceImpl
 * @Description: 回滚类
 */
@Component
public class OrderBridgeServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(OrderBridgeServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private BuySnapshotDao buySnapshotDao;

    @Reference
    private ProductService productService;

    @Transactional(rollbackFor = Exception.class)
    public void orderCreateRollBack(Order order,BuySnapshot buySnapshot, String productId, Integer count) throws ServiceException{
        try {
            orderDao.insert(order);
            buySnapshotDao.insert(buySnapshot);
            productService.deductStock(productId, order.getId(), count);
            Integer result = orderDao.updateOrderStatus(order.getId(), order.getUserId(), OrderStatus.ORDER_PAY_WAIT.getStatus());
            if (result <= 0) {
                throw new ServiceException(ServiceCode.ORDER_STATUS_UPDATE_FAIL);
            }
        }catch (ServiceException serviceException){
            throw serviceException;
        }catch (Exception e) {
            logger.info("update less stock exception productId={} orderId={} e={}", productId, order.getId());
            throw e;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void orderCancelRollBack(String orderId, Long uid, String productId) {
        Integer updateNum = orderDao.updateOrderStatus(orderId, uid, OrderStatus.ORDER_CANCEL.getStatus());
        if (updateNum <= 0) {
            throw new ServiceException(ServiceCode.FAILD, "请不要重复提交");
        }
        try {
            productService.increaseStock(productId, orderId);
        }catch (Exception e){
            logger.info("update add stock exception productId={} orderId={} e={}", productId, orderId, e);
        }
    }


}
