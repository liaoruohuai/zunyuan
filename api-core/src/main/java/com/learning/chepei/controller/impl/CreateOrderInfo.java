package com.learning.chepei.controller.impl;

/**
 * Created by zhonghua on 2017/6/16.
 */
public class CreateOrderInfo {
    private String orderId;// 订单号YYYYMMDD+NNNNNNNN
    private String returnCode;// 结果状态编码：success/error
    private String returnMsg;// 结果信息：核销凭证码/错误信息

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }
}
