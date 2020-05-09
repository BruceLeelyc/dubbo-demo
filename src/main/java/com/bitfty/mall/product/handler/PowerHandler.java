package com.bitfty.mall.product.handler;

import com.bitfty.mall.dao.ProductDao;
import com.bitfty.mall.dao.ProductStoreRecordDao;
import com.bitfty.mall.dto.ProductOptDto;
import com.bitfty.mall.entity.Product;
import com.bitfty.mall.entity.ProductStoreRecord;
import com.bitfty.mall.enums.ProductStatus;
import com.bitfty.mall.enums.ProductStoreStatus;
import com.bitfty.mall.exception.ServiceCode;
import com.bitfty.mall.exception.ServiceException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author zhangmi
 */
@Component
public class PowerHandler implements ProductHandler {

	private static final Logger logger = LoggerFactory.getLogger(ProductHandler.class);

	@Autowired
	private ProductDao productDao;

	@Autowired
	private ProductStoreRecordDao productStoreRecordDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String deductStock(ProductOptDto optDto) throws ServiceException{
		logger.info("【减】库存操作, ProductOptDto={}", optDto);
		String productId = optDto.getId();
		String orderId = optDto.getOrderId();
		int soldCount = optDto.getSellCount();

		// 判断下单条件
		Product product = productDao.findById(productId);
		if(null == product){
			throw new ServiceException(ServiceCode.PRODUCT_NOT_EXIST);
		}
		Integer status = product.getStatus();
		Date soldDate = product.getSoldDate();
		// 产品不存在,售卖日期未配置,状态不是售卖,购买时间小于售卖日期
		if(soldDate == null || null == soldDate
				|| status != ProductStatus.SELL.getStatus() || new Date().before(soldDate)){
			throw new ServiceException(ServiceCode.STOCK_REDUCE_UNUSABLE);
		}
		// 购买产品数量超出库存
		if(product.getStoreCount() < soldCount){
			throw new ServiceException(ServiceCode.PRODUCT_STOCK_NOT_ENOUGH, "产品库存不足");
		}
		// 插入库存操作记录
		String recordId = insertRecordFlag(orderId, soldCount, product);
		// 进行减库存
		int increaseFlag = productDao.deductStock(optDto.getId(), optDto.getSellCount());
		if (increaseFlag <= 0) {
			logger.info("【减】库存失败. orderId={}", optDto.getOrderId());
			throw new ServiceException(ServiceCode.FAILD, "修改库存失败.");
		}
		logger.info("【减】库存操作成功, orderId={}", optDto.getOrderId());
		return recordId;
	}

	@Override
	public String increaseStock(ProductOptDto optDto) {
		logger.info("【加】库存操作, optDto={}", optDto);
		// 数量是否正确
		String recordId = DigestUtils.md5Hex(optDto.getId() + "_" + optDto.getOrderId());
		ProductStoreRecord subRecord = productStoreRecordDao.selectByPrimaryKey(recordId);
		if (null == subRecord) {
			logger.info("【加】库存操作受限. 记录不存在");
			throw new ServiceException(ServiceCode.PRODUCT_STOCK_LIMIT, "操作库存有误,记录不存在.");
		}

		int increaseFlag = productDao.increaseStock(optDto.getId(), subRecord.getBuyCount());
		if (increaseFlag <= 0) {
			logger.info("【加】库存修改失败. orderId={}", optDto.getOrderId());
			throw new ServiceException(ServiceCode.FAILD, "操作库存失败.");
		}

		int storeFlag = productStoreRecordDao.updateRecordStatusById(subRecord.getId(), ProductStoreStatus.FAIL.getStatus());
		if (storeFlag <= 0) {
			logger.info("【加】库存操作库存记录失败. orderId={}", optDto.getOrderId());
			throw new ServiceException(ServiceCode.FAILD, "库存记录修改失败.");
		}

		logger.info("【加】库存操作成功, id={}", optDto.getId());
		return subRecord.getId();
	}

	@Override
	public Boolean validateProduct(ProductOptDto productOptDto, boolean flag) {
		String productId = productOptDto.getId();
		int sellCount = productOptDto.getSellCount();
		Product product = productDao.findById(productId);
		if(product == null || ProductStatus.DOWN.getStatus() == product.getStatus()){
			throw new ServiceException(ServiceCode.PRODUCT_STATUS_ERROR,"产品不存在或已下架");
		}
		if(ProductStatus.SELL_OUT.getStatus() == product.getStatus()){
			throw new ServiceException(ServiceCode.PRODUCT_STATUS_ERROR,"产品已售罄");
		}
		if(ProductStatus.INIT.getStatus() == product.getStatus() || product.getSoldDate() == null || new Date().compareTo(product.getSoldDate()) < 1){
			throw new ServiceException(ServiceCode.PRODUCT_STATUS_ERROR,"产品未开始售卖");
		}
		if(flag && sellCount > product.getStoreCount()){
			throw new ServiceException(ServiceCode.PRODUCT_STOCK_NOT_ENOUGH,"产品库存不足");
		}
		return true;
	}

	/**
	 * 插入库存操作记录
	 */
	private String insertRecordFlag(String orderId, int soldCount, Product product) {
		String md5Id = DigestUtils.md5Hex(product.getId() + "_" + orderId);
		ProductStoreRecord record = new ProductStoreRecord();
		record.setId(md5Id);
		record.setProdId(product.getId());
		record.setOrderId(orderId);
		record.setName(product.getName());
		record.setStoreCount(product.getStoreCount()-soldCount);
		record.setSoldCount(product.getSoldCount()+soldCount);
		record.setBuyCount(soldCount);
		record.setStatus(ProductStoreStatus.HANDLE.getStatus());
		record.setType(ProductStoreRecord.TYPE_SUB);
		int insertFlag = productStoreRecordDao.insertSelective(record);
		if (insertFlag <= 0) {
			throw new ServiceException(ServiceCode.FAILD, "插入库存记录失败");
		}
		return md5Id;
	}
}
