package com.bitfty.mall.product.handler;

import com.bitfty.mall.dto.ProductOptDto;
import com.bitfty.mall.exception.ServiceException;

/**
* @ClassName: ProductHandler
* @Description: 算力
* @author: lixl
* @date: 2019/12/12 11:37
*/
public interface ProductHandler {

    /**
     * 扣除库存
     * @param optDto 操作参数
     * @return recordId 记录ID
     * @throws ServiceException 业务异常信息
     *         1000-失败
     *         3018-产品不可用
     *         3019-库存数量超限
     */
    String deductStock(ProductOptDto optDto) throws ServiceException;

    /**
     * 添加库存
     * @param optDto 操作参数
     * @return 操作标识
     * @throws ServiceException 业务异常信息
     *         1000-失败
     *         3019-库存数量超限
     */
    String increaseStock(ProductOptDto optDto) throws ServiceException;
    /**
     * 验证库存操作
     * 非法操作说明:(产品不存在,售卖日期未配置,状态不是售卖中,购买时间在售卖日期之前)
     * @param productOptDto productOptDto
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
    Boolean validateProduct(ProductOptDto productOptDto, boolean flag);
}
