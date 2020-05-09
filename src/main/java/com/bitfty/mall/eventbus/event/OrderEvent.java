package com.bitfty.mall.eventbus.event;


import com.bitfty.eventBus.Event;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: OrderEvent
 * @Description: 订单处理成功，通知产品
 * @Author: gjw
 * @Date: 2019/12/10
 */
@Data
public class OrderEvent implements Event {

    /**
     * 订单id
     * */
    private String orderId;

    /**
     * 订单状态
     * */
    private Integer status;
    /**
     * 订单时间
     * */
    private Date createAt;

}
