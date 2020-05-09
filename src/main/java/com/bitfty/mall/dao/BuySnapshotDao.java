package com.bitfty.mall.dao;

import com.bitfty.mall.entity.BuySnapshot;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BuySnapshotDao {

    Integer insert(BuySnapshot productSnapshot);

    List<BuySnapshot> queryBuySnapshotByOrderId(String orderId);

	List<BuySnapshot> findSnapshotByProdIds(List<String> productIds);

    Integer updateCurrencyTypeAmount(@Param("id") String id, @Param("currencyTypeAmount") BigDecimal currencyTypeAmount);

    List<BuySnapshot> queryBuySnapshotByOrderIds(@Param("list") List<String> orderIds);

    List<BuySnapshot> findSnapshotByIds(@Param("list") List<String> snopIds);

    List<BuySnapshot> findByProductTypeAndLurce(@Param("uid")String uid, @Param("productType") Integer productType, @Param("type") Integer type, @Param("time") Date time , @Param("start") int i, @Param("end")Integer pageSize);

    Integer getCountByProductTypeAndLurce(@Param("uid")String uid, @Param("productType") Integer productType, @Param("type") Integer type, @Param("time") Date time);

    BuySnapshot findbyOrderId(String orderId);

    void updateCurrencyTypeAmountTime(@Param("orderId")String orderId, @Param("currencyType")String currencyType, @Param("realGainDate")Date realGainDate, @Param("realExpireDate")Date realExpireDate);
}
