package com.learning.chepei;

import com.learning.util.basic.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

public class ResponseCode {
    public static Map<String, String> codeMap = new HashMap<>();

    static {
        codeMap.put("00", "交易成功");
        codeMap.put("0001", "查发卡行");
        codeMap.put("0003", "无效商户");
        codeMap.put("0004", "受限商户");
        codeMap.put("0006", "无效合同");
        codeMap.put("0007", "终端已经下载过TMK");
        codeMap.put("0008", "终端未签到");
        codeMap.put("0012", "无效交易");
        codeMap.put("0013", "无效金额-累计退货金额、调账金额等");
        codeMap.put("0014", "无效卡号 包括销毁状态");
        codeMap.put("0015", "库存中无此卡号");
        codeMap.put("0016", "卡未激活");
        codeMap.put("0017", "与原交易卡号不符");
        codeMap.put("0020", "无效应答");
        codeMap.put("0021", "账户不匹配");
        codeMap.put("0022", "怀疑操作有误");
        codeMap.put("0023", "不可接受的手续费");
        codeMap.put("0025", "未找到原交易");
        codeMap.put("0026", "原交易不成功");
        codeMap.put("0031", "路由失败-机构不支持");
        codeMap.put("0034", "有作弊嫌疑");
        codeMap.put("0036", "卡已锁定");
        codeMap.put("0039", "交易日期有误");
        codeMap.put("0040", "请求的功能尚不支持");
        codeMap.put("0041", "卡已挂失");
        codeMap.put("0042", "无效账户 商户合同下未关联账户");
        codeMap.put("0044", "卡被注销");
        codeMap.put("0045", "卡被冻结");
        codeMap.put("0051", "余额不足");
        codeMap.put("0054", "过期的卡");
        codeMap.put("0055", "密码错");
        codeMap.put("0056", "无此卡记录");
        codeMap.put("0057", "不允许持卡人进行的交易");
        codeMap.put("0058", "不允许终端进行的交易");
        codeMap.put("0061", "POS单笔交易金额超限");
        codeMap.put("0062", "POS当天累计交易金额超限");
        codeMap.put("0063", "余额不正确");
        codeMap.put("0064", "与原交易金额不符");
        codeMap.put("0065", "消费次数超限");
        codeMap.put("0066", "充值次数超限");
        codeMap.put("0067", "网上单笔交易金额超限");
        codeMap.put("0068", "网上当天累计交易金额超限");
        codeMap.put("0072", "无效IC卡信息");
        codeMap.put("0073", "cvv不正确");
        codeMap.put("0074", "cvv2不正确");
        codeMap.put("0075", "密码错误次数超限");
        codeMap.put("0077", "pos批次不一致重新签到");
        codeMap.put("0090", "系统正在批处理日切中");
        codeMap.put("0091", "通信失败");
        codeMap.put("0092", "设施找不到或无法达到");
        codeMap.put("0094", "重复交易");
        codeMap.put("0095", "加密失败");
        codeMap.put("0096", "系统故障");
        codeMap.put("0097", "无效终端");
        codeMap.put("0099", "PIN格式错误");
        codeMap.put("0100", "门店号有误");
        codeMap.put("00A0", "MAC错");
        codeMap.put("0092", "重复提交订单");
        codeMap.put("0009", "文件不存在");
        codeMap.put("0010", "文件打开错误");
        codeMap.put("0043", "无效客户");
        codeMap.put("0046", "无效地址");
        codeMap.put("9999", "系统错误");
        codeMap.put("9902", "报文格式错误");
        codeMap.put("0102", "报文字段值检查错误（内容不允许空、长度不符合等）");
        codeMap.put("0103", "机构号有误（找不到）");
        codeMap.put("0104", "手机号码格式有误");
        codeMap.put("0105", "证件类型有误");
        codeMap.put("0106", "证件号有误");
        codeMap.put("0107", "性别有误");
        codeMap.put("0108", "购卡人类型暂不支持企业类型");
        codeMap.put("0109", "请勿重复提交");
        codeMap.put("0110", "原有卡与新卡的卡产品不匹配");
        codeMap.put("0111", "未找到客户或持卡人信息");
        codeMap.put("0112", "证件号、证件类型与持卡人不匹配");
        codeMap.put("0113", "原有卡已注销");
    }


    public static String getMessageByCode(String code) {
        String message = codeMap.get(code);
        return ObjectUtil.isEmpty(message) ? "未知错误" : message;
    }
}
