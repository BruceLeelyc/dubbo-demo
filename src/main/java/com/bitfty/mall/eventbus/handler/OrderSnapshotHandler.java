package com.bitfty.mall.eventbus.handler;

import com.bitfty.eventBus.EventBusHandler;
import com.bitfty.mall.eventbus.event.OrderEvent;
import com.bitfty.user.service.BillService;
import com.bitfty.user.service.LurceService;
import com.google.common.eventbus.Subscribe;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.text.ParseException;

/**
 * @ClassName: OrderSnapshotHandler
 * @Description: 下单完成后的快照处理
 * @Author: lzk
 * @Date: 2019/12/4 15:12
 */
@Component
public class OrderSnapshotHandler implements EventBusHandler {

    private final static Logger logger = LoggerFactory.getLogger(OrderSnapshotHandler.class);

    @Reference
    private LurceService lurceService;

    @Reference
    private BillService billService;

    /**
     * 生成用户收益规则计算
     * @param orderSnapshotEvent
     */
    @Subscribe
    public void billRule(OrderEvent orderSnapshotEvent) throws ParseException {
        lurceService.gengerateLurce(orderSnapshotEvent.getOrderId());
    }

    /**
     * 生成用户所有电费费用账单
     * @param orderSnapshotEvent
     */
    @Subscribe
    public void electricFee(OrderEvent orderSnapshotEvent) throws ParseException {
        billService.generateBillRule(orderSnapshotEvent.getOrderId());
    }

}
