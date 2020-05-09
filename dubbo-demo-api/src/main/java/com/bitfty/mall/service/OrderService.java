package com.bitfty.mall.service;

import com.bitfty.mall.dto.*;
import com.bitfty.util.Page;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderService {

    Map<String,Integer> querySystemIndexReport();

    OrderCreateReturnDto create(Long uid, String productId, Integer count,Integer days);

    Page<OrderListDto> page(Integer pageNo, Integer pageSize, Map<String, Object> params);

    void update(OrderDto orderDto);

    Boolean cancel(String id, Long uid);

    OrderDto queryByUid(Long uid);

    OrderDto queryByIdAndUid(Long uid, String id);

    List<Map<String,Object>> queryOrderIds();

    /**
     * 查询处理中订单
     * @param orderIdList 订单id
     * @return list
     */
	List<String> findInHandleOrderByIds(List<String> orderIdList);

    Boolean cancelOrderForJob(String id, Long uid, String prodId);

    OrderDto findByIdStatus( String orderId, int status, Long uid);
    /**
     * 根据状态查询所有订单编号
     * @return
     */
    List<String> findByStatus(int status);

    /**
     * 根据订单号查询订单信息
     * @param orderIds 订单id
     * @return list
     */
    List<OrderDto> findOrderByIds(List<String> orderIds);

    /**
     * 将订单状态为1 的置为stauts
     * @param orderid
     */
    void finishOrderStatus(String orderid,String currencyType);

    Page<OrderBackstageDto> pageBackstage(Integer pageNo, Integer pageSize, Map<String, Object> params);

    OrderDetailDto queryOrderDetail(Long uid, String id);

    OrderDto queryById(String orderId);

    List<String> findByStatusAndTime(int status, Date startTime);
}
