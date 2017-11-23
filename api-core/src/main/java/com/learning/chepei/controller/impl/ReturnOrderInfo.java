package com.learning.chepei.controller.impl;

/**
 * Created by zhonghua on 2017/6/16.
 */
public class ReturnOrderInfo {

    private String orgOrderId;// 原订单号
    private String returnCode;// 结果状态编码：success/error
    private String returnMsg;// 结果信息：核销凭证码/错误信息

    public String getOrgOrderId() {
        return orgOrderId;
    }

    public void setOrgOrderId(String orgOrderId) {
        this.orgOrderId = orgOrderId;
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
