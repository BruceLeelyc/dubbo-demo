package com.bitfty.mall.eventbus.event;

import com.bitfty.eventBus.Event;
import lombok.Data;

/**
 * @author ：liujipeng
 * @date ：Created in 2019/12/9 13:21
 * @description：${description}
 */
@Data
public class TradeEvent implements Event {

    private boolean flag;

    private String orderId;

    private String tradeNo;

}
