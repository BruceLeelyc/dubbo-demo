//package com.bitfty.mall;
//
//import com.bitfty.enums.ProductType;
//import com.bitfty.mall.dao.ProductDao;
//import com.bitfty.mall.dto.BuySnapshotDto;
//import com.bitfty.mall.dto.ProductDto;
//import com.bitfty.mall.dto.ProductOptDto;
//import com.bitfty.mall.dto.ProductStoreRecordDto;
//import com.bitfty.mall.entity.Product;
//import com.bitfty.mall.enums.ProductOperate;
//import com.bitfty.mall.exception.ServiceCode;
//import com.bitfty.mall.exception.ServiceException;
//import com.bitfty.mall.product.ProductFactory;
//import com.bitfty.mall.product.handler.ProductHandler;
//import com.bitfty.mall.service.BuySnapshotService;
//import com.bitfty.mall.service.OrderService;
//import com.bitfty.mall.service.ProductService;
//import com.bitfty.mall.service.ProductStoreRecordService;
//import com.bitfty.mall.utils.DistributedLock;
//import com.bitfty.rws.DataSourceType;
//import com.bitfty.rws.DynamicDataSourceContextHolder;
//import com.bitfty.util.Page;
//import org.apache.dubbo.config.annotation.Reference;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Handler;
//import java.util.stream.Collectors;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@ActiveProfiles(profiles = "online")
//public class ProductTest extends BaseTest{
//
//	private static final Logger logger = LoggerFactory.getLogger(ProductTest.class);
//
//	@Reference
//	private ProductService productService;
//
//	@Reference
//	private BuySnapshotService buySnapshotService;
//
//	@Reference
//	private OrderService orderService;
//
//	@Reference
//	private ProductStoreRecordService productStoreRecordService;
//
//	@Autowired
//	private ProductDao productDao;
//
//	@Test
//	public void testProductRecord() {
//		List<ProductStoreRecordDto> handleRecordByDate = productStoreRecordService.findHandleRecordByDate(new Date());
//		System.out.println(handleRecordByDate.size());
//	}
//
//	@Test
//	public void testUpdate() {
//		productService.deductStock("aaf","aaf",2);
//		//productService.increaseStock("aaf","aaf",2);
//		/*List<String> updateProdIds = Arrays.asList("a", "b", "c");
//		int count = productService.updateStatusDownByIds(updateProdIds);
//		System.out.println(count);*/
//	}
//
//	@Test
//	public void testProduct() {
//		// 1.获取库存为0的所有产品 p0
//		List<String> productIdList = productService.findSellOutProdIdList();
//		List<BuySnapshotDto> snapshotDtoList = buySnapshotService.findSnapshotByProdIds(productIdList);
//		// List<BuySnapshotDto> snapshotDtoList = new ArrayList<>();
//		/*BuySnapshotDto buySnapshotDto = new BuySnapshotDto();
//		buySnapshotDto.setOrderId("ac");
//		buySnapshotDto.setProdId("12");
//		snapshotDtoList.add(buySnapshotDto);
//		BuySnapshotDto buySnapshotDto2 = new BuySnapshotDto();
//		buySnapshotDto2.setOrderId("ad");
//		buySnapshotDto2.setProdId("21");
//		snapshotDtoList.add(buySnapshotDto2);
//		BuySnapshotDto buySnapshotDto3 = new BuySnapshotDto();
//		buySnapshotDto3.setOrderId("12");
//		buySnapshotDto3.setProdId("12");
//		snapshotDtoList.add(buySnapshotDto3);
//		BuySnapshotDto buySnapshotDto4 = new BuySnapshotDto();
//		buySnapshotDto4.setOrderId("21");
//		buySnapshotDto4.setProdId("21");
//		snapshotDtoList.add(buySnapshotDto4);
//		BuySnapshotDto buySnapshotDto5 = new BuySnapshotDto();
//		buySnapshotDto5.setOrderId("ab");
//		buySnapshotDto5.setProdId("12");
//		snapshotDtoList.add(buySnapshotDto5);
//		BuySnapshotDto buySnapshotDto6 = new BuySnapshotDto();
//		buySnapshotDto6.setOrderId("aa");
//		buySnapshotDto6.setProdId("21");
//		snapshotDtoList.add(buySnapshotDto6);
//		BuySnapshotDto buySnapshotDto7 = new BuySnapshotDto();
//		buySnapshotDto7.setOrderId("f");
//		buySnapshotDto7.setProdId("1212");
//		snapshotDtoList.add(buySnapshotDto7);*/
//		// 2.判断p0对应的订单状态是否存在中间状态
//		List<String> orderIdList = snapshotDtoList.parallelStream().map(dto -> dto.getOrderId()).collect(Collectors.toList());
//		List<String> handleOrderIs = orderService.findInHandleOrderByIds(orderIdList);
//		/*handleOrderIs.add("aa");
//		handleOrderIs.add("ab");
//		handleOrderIs.add("12");*/
//		List<String> selloutProd = snapshotDtoList.parallelStream().filter(dto -> handleOrderIs.contains(dto.getOrderId()))
//				.map(prod -> prod.getProdId()).collect(Collectors.toList());
//		List<String> updateProdIds = productIdList.parallelStream().filter(prodId -> !selloutProd.contains(prodId)).collect(Collectors.toList());
//		// 3.没有则更新产品为下架状态
//		int count = productService.updateStatusDownByIds(updateProdIds);
//		System.out.println("修改产品数量=" + count);
//	}
//
//	@Test
//	public void shouldAnswerWithTrue() {
//		// 锁定产品
//		String key = "PRODUCT_STOCK_LOCK_11111";
//		if (!DistributedLock.lock(key, 3)) {
//			throw new ServiceException(ServiceCode.FAILD, "操作太频繁请稍后操作");
//		}
//		try {
//			String productId = "1";
//			int soldCount = 1;
//			ProductType productOpt = ProductType.POWER;
//			ProductHandler handler = ProductFactory.handler(productOpt);
//			ProductOptDto productOptDto = ProductOptDto.builder().id(productId).sellCount(soldCount).build();
//			if (productOpt.getType() == ProductType.POWER.getType()) {
//				handler.deductStock(productOptDto);
//			} else {
//				handler.increaseStock(productOptDto);
//			}
//		} catch (Exception e) {
//			logger.error("扣减库存异常.e={}", e.getMessage(), e);
//			throw new ServiceException(ServiceCode.FAILD, "操作失败.");
//		} finally {
//			DistributedLock.unLock(key);
//		}
//	}
//
//
//	@Test
//	public void mysqlMasterSlave() {
//		try {
//			List<String> productIdList = productService.findSellOutProdIdList();
//			logger.info("主{}",productIdList);
//			DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.MASTER.getType());
//			int count1 = productDao.countByParams(new HashMap<>());
//			logger.info("主2{}",count1);
////			DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.SLAVE.getType());
////			int count2 = productDao.countByParams(new HashMap<>());
////			logger.info("数量从{}",count2);
//		} catch (Exception e) {
//			logger.error("扣减库存异常.e={}", e.getMessage(), e);
//			throw new ServiceException(ServiceCode.FAILD, "操作失败.");
//		}
//	}
//}
