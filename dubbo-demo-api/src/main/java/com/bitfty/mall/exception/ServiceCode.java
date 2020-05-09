package com.bitfty.mall.exception;

/**
* @ClassName: ServiceCode
* @Description: enum
* @author: group
* @date: 2019/12/12 10:53
*/
public enum ServiceCode {

    SUCCESS(0,"成功"),

    FAILD(1000,"失败"),

    REDIS_LOCK(1001,"操作太频繁请稍后操作"),

    STOCK_REDUCE_FAIL(3001,"减库存失败"),

    STOCK_ADD_FAIL(3002,"加库存失败"),

    ORDER_CANCEL_FAIL(3003,"订单取消异常"),

    ORDER_CREATE_FAIL(3004,"订单创建异常"),

    ORDER_STORE_FAIL(3005,"冻结库存记录不存在"),

    URL_NOT_EXIST(3005,"接口地址不存在"),



    ACCOUNT_NOT_DISABLE (3011,"该币种对应的账户不可用"),

    GOOGLE_VALIDATE_FAILED (3012,"谷歌验证失败"),

    USER_NOT_EXIST(3013,"用户不存在"),

    ORDER_AMOUNT_NOT_EXIST(3014,"订单充值金额不存在"),

    ORDER_STATUS_UPDATE_FAIL(3016,"订单状态修改失败"),

    STOCK_REDUCE_EXCEPTION(3017,"减库存异常"),

    STOCK_REDUCE_UNUSABLE(3018,"产品不可用"),

    PRODUCT_STOCK_LIMIT(3019,"库存数量有误"),

    PRODUCT_STATUS_ERROR(3020,"产品状态不符合购买"),

    PRODUCT_STOCK_NOT_ENOUGH(3021,"产品库存不足"),

    PRODUCT_NOT_EXIST(3022,"产品不存在"),

    ORDER_NOT_EXIST(3023,"订单查询异常"),

    BUYSNAPSHOT_NOT_EXIST(3025,"订单快照不存在"),

    INTERNAL_ERROR(3026,"系统内部错误"),

    PARAMS_ERROR(3027,"参数有误"),

    REQUEST_FREQUENT(3028,"您请求的太频繁啦！请稍后再试"),

    EXCHANGE_RATE_GET_NULL(3029,"获取汇率为空"),

    EXCHANGE_RATE_BE_OVERDUE(3030,"当前汇率已过期，请获取最新的汇率"),

    ELASTIC_SEARCH_EXCEPTION(3031,"数据检索异常"),

    ARTICLE_VOTE_REPEAT(3032,"已提交"),

    ARTICLE_VOTE_UPDATE_BY_QUERY(3033,"投票异常，请稍后再试"),

    SERVICE_UNAVAILABLE(5000,"系统内部错误"),
    ;


    private int status;

    private String msg;

    ServiceCode(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
