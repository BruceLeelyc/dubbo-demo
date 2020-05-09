package com.bitfty.mall.service;

import com.bitfty.mall.dto.ProductDto;
import com.bitfty.mall.exception.ServiceException;
import com.bitfty.util.Page;

import java.util.List;
import java.util.Map;

/**
* @ClassName: ProductService
* @Description: 产品接口
* @author: zhangmi
* @date: 2019/12/12 10:19
*/
public interface ProductService {

    Page<ProductDto> pageGeteway(Integer pageNo, Integer pageSize, Map<String, Object> params);

    Page<ProductDto> page(Integer pageNo, Integer pageSize, Map<String, Object> params);

    List<ProductDto> findRecommend(Map<String, Object> params);

    int save(ProductDto product);

    int update(ProductDto product);

    ProductDto findById(String id);

    /**
     * 减库存操作
     * 非法操作说明:(产品不存在,售卖日期未配置,状态不是售卖中,购买时间在售卖日期之前)
     * @param productId 产品id
     * @param orderId 订单id
     * @param soldCount 购买数量
     * @throws ServiceException 业务异常信息
     *         1000-失败
     *         1001-操作太频繁请稍后操作
     *         3018-产品不可用
     *         3019-库存数量超限
     *         7000-参数有误
     * @return string 操作库存记录ID
     */
    String deductStock(String productId, String orderId, int soldCount) throws ServiceException;

    /**
     * 加库存操作
     * @param productId 产品id
     * @param orderId 订单id
     * @throws ServiceException 业务异常信息
     *         1000-失败
     *         1001-操作太频繁请稍后操作
     *         3019-库存数量有误
     *         7000-参数有误
     * @return string 操作返回标识
     */
    String increaseStock(String productId, String orderId) throws ServiceException;

    /**
     * 查询所有已售罄产品
     * @return List<String> 产品ID
     */
    List<String> findSellOutProdIdList();

    /**
     * 更新产品为售罄
     * @param updateProdIds 更新id
     * @return 更新数量
     */
    int updateStatusDownByIds(List<String> updateProdIds);

    /**
     * 根据ID更新产品状态
     * @param id ， status
     * @return
     */
   int updateStatusById(String id,String status);


    /**
     * 验证库存操作
     * 非法操作说明:(产品不存在,售卖日期未配置,状态不是售卖中,购买时间在售卖日期之前)
     * @param productId 产品id
     * @param soldCount 购买数量
     * @param flag 是否验证库存
     * @throws ServiceException 业务异常信息
     *         3020-状态不符合{
     *             产品不存在或已下架，
     *             产品已售罄
     *             产品未开始售卖
     *         }
     *         3021-产品库存不足
     *         7000-参数有误
     * @return true 校验通过
     */
    Boolean validateProduct(String productId, int soldCount, boolean flag) throws ServiceException;
}
