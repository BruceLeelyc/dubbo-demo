package com.bitfty.mall.dao;

import com.bitfty.mall.dto.OrderListDto;
import com.bitfty.mall.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {

    Integer insert(Order order);

    List<OrderListDto> findPageByParams(Map<String,Object> params);

    Integer update(Order order);

    Integer countByParams(Map<String,Object> params);

    Integer countByStatus(Map<String,Object> params);

    Order findByIdStatus(@Param("orderId") String orderId,@Param("status") int status,@Param("uid") Long uid);

    Integer cancel(@Param("id") String id, @Param("uid")Long uid);

    Order queryByUid(@Param("uid") Long uid);

    Order queryById(@Param("id")String id);

    Order queryByIdAndUid(@Param("uid")Long uid, @Param("id")String id);

    List<Map<String,Object>> queryOrderIds();

	List<String> findInHandleOrderByIds(List<String> orderIdList);

    Integer updateOrderStatus(@Param("orderId") String orderId,@Param("uid") Long uid ,@Param("status") Integer status);

    BigDecimal queryAmount(@Param("id") String id, @Param("uid") Long uid);

    List<Order> findOrderByIds(List<String> orderIds);

    /**
     * 获取状态为 status 订单id
     * @return
     */
    List<String> findByStatus(@Param("status") int status);
    /**
     * 将状态为 未支付的订单 状态设置成 status 状态
     * @return*/

    Integer finishOrderStatus(@Param("orderId") String orderId,@Param("currencyType") String currencyType);

    List<Order> findPageBackstageByParams(Map<String ,Object> params);

    Integer countPageBackstageByParams(Map<String ,Object> params);

    List<String> findIdByStatusAndTime(@Param("status")Integer status,@Param("startTime") Date date);
}
