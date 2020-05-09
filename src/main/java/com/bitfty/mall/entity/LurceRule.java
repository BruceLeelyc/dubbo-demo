package com.bitfty.mall.entity;

import java.math.BigDecimal;
import java.util.Date;

public class LurceRule {
    private String id;

    private String userId;

    private String orderId;

    private String productSnapshotId;

    private Date startTime;

    private Date endTime;

    private Integer beenTime;

    private String currencyType;

    private BigDecimal beenAmount;

    private BigDecimal serviceFee;

    private Integer lurceType;

    private String powerSize;

    private String channel;

    private Integer status;

    private Date createAt;

    private Date updateAt;

    private String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getProductSnapshotId() {
        return productSnapshotId;
    }

    public void setProductSnapshotId(String productSnapshotId) {
        this.productSnapshotId = productSnapshotId == null ? null : productSnapshotId.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getBeenTime() {
        return beenTime;
    }

    public void setBeenTime(Integer beenTime) {
        this.beenTime = beenTime;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    public BigDecimal getBeenAmount() {
        return beenAmount;
    }

    public void setBeenAmount(BigDecimal beenAmount) {
        this.beenAmount = beenAmount;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Integer getLurceType() {
        return lurceType;
    }

    public void setLurceType(Integer lurceType) {
        this.lurceType = lurceType;
    }

    public String getPowerSize() {
        return powerSize;
    }

    public void setPowerSize(String powerSize) {
        this.powerSize = powerSize == null ? null : powerSize.trim();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel == null ? null : channel.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }
}