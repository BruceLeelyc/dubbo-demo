package com.bitfty.mall.dao;


import com.bitfty.mall.dto.ProductOptDto;
import com.bitfty.mall.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhangmi
 */
public interface ProductDao {

    Product queryById(@Param("id") String id);

    Integer updateStoreCountBuId(@Param("id") String id, @Param("count") Integer count);

    Product findById(String id);

    int delete(String id);

    int insert(Product record);

    int update(Product record);

    List<Product> findPageByParams(Map<String, Object> params);

    List<Product> findPage(Map<String, Object> params);

    int countByParams(Map<String, Object> params);

    List<Product> findRecommend(Map<String, Object> params);


    /**
     * 产品库-扣减
     *
     * @param prodId    产品id
     * @param count 操作数量
     * @return
     */
	int deductStock(@Param("prodId") String prodId, @Param("count") int count);

    /**
     * 增加库存
     *
     * @param prodId    产品id
     * @param count 操作数量
     * @return
     */
    int increaseStock(@Param("prodId") String prodId, @Param("count") int count);

    /**
     * 查询已售罄产品ID
     *
     * @return
     */
    List<String> findSellOutProdIdList();

    /**
     * 更新产品为售罄
     *
     * @param updateProdIds
     * @return
     */
    int updateStatusDownByIds(List<String> updateProdIds);

    /**
     * 更新产品状态
     *
     * @param id status
     * @return
     */
    int updateStatusById(@Param("id") String id, @Param("status") String status);

}