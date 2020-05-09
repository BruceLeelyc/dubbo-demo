package com.bitfty.mall.service;

import com.bitfty.mall.dao.BuySnapshotDao;
import com.bitfty.mall.dto.BuySnapshotDto;
import com.bitfty.mall.entity.BuySnapshot;
import com.bitfty.util.BeanMapper;
import com.bitfty.util.Page;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BuySnapshotServiceImpl implements BuySnapshotService {

	@Autowired
	private BuySnapshotDao buySnapshotDao;

	@Override
	public List<BuySnapshotDto> findSnapshotByProdIds(List<String> productIds) {
		List<BuySnapshot> buySnapshotList = buySnapshotDao.findSnapshotByProdIds(productIds);
		if (CollectionUtils.isEmpty(buySnapshotList)) {
			return new ArrayList<>();
		}
		return BeanMapper.mapList(buySnapshotList, BuySnapshotDto.class);
	}

	@Override
	public List<BuySnapshotDto> queryBuySnapshotByOrderId(String orderId) {
		List<BuySnapshot> buySnapshot = buySnapshotDao.queryBuySnapshotByOrderId(orderId);
		if (CollectionUtils.isEmpty(buySnapshot)) {
			return new ArrayList<>();
		}
		return BeanMapper.mapList(buySnapshot, BuySnapshotDto.class);
	}

	@Override
	public List<BuySnapshotDto> findByOrderIds(List<String> orderIds) {
		List<BuySnapshot> buySnapshot = buySnapshotDao.queryBuySnapshotByOrderIds(orderIds);
		return BeanMapper.mapList(buySnapshot, BuySnapshotDto.class);
	}

	@Override
	public BuySnapshotDto findByOrderId(String orderId){
		return BeanMapper.map(buySnapshotDao.findbyOrderId(orderId), BuySnapshotDto.class);
	}

	@Override
	public void updateOrderInfo(String orderId, String currencyType, Date realGainDate, Date realExpireDate){
		buySnapshotDao.updateCurrencyTypeAmountTime(orderId,currencyType,realGainDate,realExpireDate);
	}

	@Override
	public List<BuySnapshotDto> findSnapshotByIds(List<String> snopIds) {
		List<BuySnapshot> buySnapshotList = buySnapshotDao.findSnapshotByIds(snopIds);
		if (CollectionUtils.isEmpty(buySnapshotList)) {
			return new ArrayList<>();
		}
		return BeanMapper.mapList(buySnapshotList, BuySnapshotDto.class);
	}

	@Override
	public Page<BuySnapshotDto> findByProducTypeAndLucre(String uid, Integer productType, Integer type, Integer pageNo, Integer pageSize) {
		Integer count = buySnapshotDao.getCountByProductTypeAndLurce(uid, productType, type, new Date(System.currentTimeMillis()+24*60*60*1000));
		if (count == null || count <=0) {
			return new Page<>();
		}
		List<BuySnapshot> buySnapshots = buySnapshotDao.findByProductTypeAndLurce(uid, productType, type, new Date(System.currentTimeMillis()+24*60*60*1000), (pageNo - 1) * pageSize, pageSize);
		return new Page<BuySnapshotDto>(count,pageSize,pageNo,BeanMapper.mapList(buySnapshots,BuySnapshotDto.class));
	}
}
