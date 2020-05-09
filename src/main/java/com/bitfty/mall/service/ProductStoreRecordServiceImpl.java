package com.bitfty.mall.service;

import com.bitfty.mall.dao.ProductStoreRecordDao;
import com.bitfty.mall.dto.ProductStoreRecordDto;
import com.bitfty.mall.entity.ProductStoreRecord;
import com.bitfty.util.BeanMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class ProductStoreRecordServiceImpl implements ProductStoreRecordService {

    @Autowired
    private ProductStoreRecordDao productStoreRecordDao;

    @Override
    public ProductStoreRecordDto findByOrderIdAndType(String id, Integer type) {
        ProductStoreRecord productStoreRecord = productStoreRecordDao.findByOrderIdAndType(id, type);
        return BeanMapper.map(productStoreRecord, ProductStoreRecordDto.class);
    }

	@Override
	public List<ProductStoreRecordDto> findRecordByStatus(int status) {
		List<ProductStoreRecord> recordList = productStoreRecordDao.findRecordByStatus(status);
		return BeanMapper.mapList(recordList, ProductStoreRecordDto.class);
	}

	@Override
	public List<ProductStoreRecordDto> findHandleRecordByDate(Date beforeDate) {
		List<ProductStoreRecord> recordList = productStoreRecordDao.findHandleRecordByDate(beforeDate);
		return BeanMapper.mapList(recordList, ProductStoreRecordDto.class);
	}

	@Override
	public int updateRecordStatusByOrderId(List<String> successList, int recordStatus) {
		return productStoreRecordDao.updateRecordStatusByOrderId(successList, recordStatus);
	}

	@Override
	public int updateRecordStatusById(String id, int status) {
		return productStoreRecordDao.updateRecordStatusById(id, status);
	}

	@Override
	public ProductStoreRecordDto findByOrderIdAndStatus(String orderId, int recordStatus) {
    	ProductStoreRecord recordEntity= productStoreRecordDao.findByOrderIdAndStatus(orderId, recordStatus);
		return BeanMapper.map(recordEntity, ProductStoreRecordDto.class);
	}

	@Override
	public List<String> findHandleByProdIds(List<String> prodIdList) {
		return productStoreRecordDao.findHandleByProdIds(prodIdList);
	}
}
