package com.bitfty.mall.dao;

import com.bitfty.mall.entity.ProductStoreRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
/**
* @ClassName: ProductStoreRecordDao
* @Description: 产品库存记录
* @author: lixl
* @date: 2019/12/12 13:52
*/
public interface ProductStoreRecordDao {
    int deleteByPrimaryKey(String id);

    int insert(ProductStoreRecord record);

    int insertSelective(ProductStoreRecord record);

    ProductStoreRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ProductStoreRecord record);

    int updateByPrimaryKey(ProductStoreRecord record);

    ProductStoreRecord findByOrderIdAndType(@Param("orderId") String orderId, @Param("type") Integer type);

	List<ProductStoreRecord> findRecordByStatus(@Param("status") int status);

    /**
     * 更新记录状态
     *
     * @param successList
     * @param recordStatus
     * @return
     */
    int updateRecordStatusByOrderId(@Param("successList") List<String> successList, @Param("status") int recordStatus);

    /**
     * 更新
     *
     * @param id     id
     * @param status 状态
     * @return 更新数量
     */
    int updateRecordStatusById(@Param("id") String id, @Param("status") int status);

    /**
     * 通过orderId和状态查询记录
     *
     * @param orderId      订单id
     * @param recordStatus 记录状态
     * @return ProductStoreRecord
     */
    ProductStoreRecord findByOrderIdAndStatus(@Param("orderId") String orderId, @Param("status") int recordStatus);

    /**
     * 通过prodId和状态查询记录
     *
     * @param prodId      产品id
     * @param recordStatus 记录状态
     * @return ProductStoreRecord
     */
    ProductStoreRecord findByProdIdAndStatus(@Param("prodId") String prodId, @Param("status") int recordStatus);

    /**
     * 通过prodIdList查询处理中记录
     *
     * @param prodIdList      产品id
     * @return ProductStoreRecord
     */
    List<String> findHandleByProdIds(List<String> prodIdList);

    /**
     * 查询N分钟之前的处理中记录
     * @param beforeDate 查询时间
     * @return List<ProductStoreRecord> 处理中记录
     */
    List<ProductStoreRecord> findHandleRecordByDate(@Param("beforeDate") Date beforeDate);
}