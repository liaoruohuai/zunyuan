package com.learning.chepei.util;

public class Constants {
//	public static final String PAD = "80";
	public static final String ENCODE_XML_GBK ="GBK";
	public static final char CZERO = '0';
	public static final String SEP = ";";
	
	public static final String PARAERR = "E1";// 参数错误
	public static final String SCODEERR = "E2";// 服务代码错误
	public static final String SYSERR = "E3";// 系统错误
	public static final String XMLENCODING = "GBK";// xml编码
	public static final String KEY_SOCKET_IP = "socket.ip";// 通信IP
	public static final String KEY_SOCKET_PORT = "socket.port";// 通信端口
	public static final String KEY_SOCKET_PORT2 = "socket.port2";// 绑卡通信端口
	public static final String KEY_SOCKET_IP_FORZHXT = "socket.ZHXT_ip";// 通信IP
	public static final String KEY_SOCKET_PORT_FORZHXT = "socket.ZHXT_port";// 通信端口
	public static final String KEY_HTTP_URL = "postUrl.url";// postIP
	public static final String KEY_SOCKET_LISTENER_PORT = "socket.listener_port";// 监听端口
	public static final String KEY_SOCKET_TIMEOUT = "socket.timeout";// 通信超时
	public static final String XMLBAOWENHEADER = "<?xml version=\"1.0\" encoding=\"GBK\"?>";// XML�?/ 服务�?
	public static final String SCODE_CARDTXNSTAQUERY = "1031";// 卡状态查�?
	public static final String SCODE_ACCOUNTBALANCEQUERY = "1021";// 预充值账户余额查�?
	public static final String SCODE_CASHRECHARGE = "1051";// 电子钱包现金充�?
	public static final String SCODE_CASHRECHARGEFLUSHES = "2051";// 电子钱包现金充�?冲正
	public static final String SCODE_RECHARGENOTIFY = "1151";// 充�?成功通知
	public static final String SCODE_ACCOUNTCONSUM = "1101";// 预充值账户消�?
	public static final String SCODE_CASHCONSUM = "1171";// 电子钱包消费
	public static final String SCODE_ACCOUNTCONSUMFLUSHES = "2101";// 预充值账户消费冲�?
	public static final String SCODE_ACCOUNTCONSUMCANCLE = "3101";// 预充值账户消费撤�?
	public static final String SCODE_ACCOUNTCONSUMCANCLEFLUSHES = "4101";// 预充值账户消费撤�?���?
	public static final String SCODE_ACCOUNTCONSUMRETURNED = "5151";// 预充值消费�?�?
	public static final String SCODE_CASHRETURNED = "5171";// 电子钱包�?��
	public static final String SCODE_SPEACCOUNTTRANSFER = "1081";// 电子钱包指定账户圈存
	public static final String SCODE_SPEACCOUNTTRANSFERNOTIFY = "1181";// 电子钱包指定账户圈存成功通知
	public static final String SCODE_SPEACCOUNTTRANSFERFLUSHES = "2081";// 电子钱包指定账户圈存冲正
	public static final String SCODE_NOSPEACCOUNTTRANSFER = "1091";// 电子钱包非指定账户圈�?
	public static final String SCODE_NOSPEACCOUNTTRANSFERNOTIFY = "1191";// 电子钱包非指定账户圈存成功�?�?
	public static final String SCODE_NOSPEACCOUNTTRANSFERFLUSHES = "2091";// 电子钱包非指定账户圈存冲�?
	public static final String SCODE_ACCOUNTPWDCHANGE = "7011";// 预充值账户密码修�?
	public static final String SCODE_ACCOUNTPWDRESET = "1019";//预充值账户密码重�?
	public static final String SCODE_CHANLSIGNIN = "8201";// 渠道签到交易
	public static final String SCODE_PERINFINQ = "8301";// 个人信息查询

	public static final String SCODE_CARDTXNDETAILINQ = "6021";// 账户交易明细查询
	public static final String SCODE_BLACKLISTDOWNLOAD = "8701";// IC卡黑名单下载
	
	public static final String SCODE_BLALISTDOWNLOAD = "6001";//IC卡黑名单下载
	public static final String SCODE_PERINF = "6011";//个人记名信息查询
	public static final String SCODE_CARDACCINFINQ = "6051";//�?账户信息查询
	public static final String SCODE_CARDHANGUP = "6061";// 卡片挂失/接挂�?
	public static final String SCODE_ACCOUNTTXNDETAILINQ = "6021";//账户交易明细查询
	public static final String SCODE_PERSONCUSTINFOADDINPUT = "6071";//个人记名信息录入
	public static final String SCODE_PERSONCUSTINFOADDAMEND = "6031";//个人记名信息修改
	public static final String SCODE_PERACCRECHARGE = "1071";//个人账户充�?

	public static final String SCODE_ACCTRANSFERMONEY = "1061";// 个人账户转账
	public static final String SCODE_OPENPERSONACCWITHNAME = "6081";// 个人记名�?��
	public final static String SCODE_ADDFLOWCARD = "6091";// 个人账户副卡添加
	public final static String  SCODE_UNBINDFLOWCARD= "6101";// 个人账户副卡解绑绑定
	public final static String  SCODE_PERSONCASHRECHARGEFLUSHES= "2071";// 个人账户充�?冲正
	public final static String  SCODE_PERFCARDINFINQLIST= "6041";// 副卡列表信息查询
	public final static String SCODE_REPLACECARD = "6151";// 个人补卡\换卡
	public final static String SCODE_CANCLEACCORRETURNCARD = "6141";// 个人账户�?���?��
	public final static String SCODE_CARDFACECHECKCARD = "1801";//根据卡面号查卡号
	public final static String SCODE_CONTACTCHECKINGFILEQUERY = "6111";// 商户对账文件查询
	public final static String SCODE_CONTACTCHECKINGFILEDOWN = "6121";// 商户对账文件下载
	public final static String SCODE_ACCOUNTBALANCEQUERYS = "1021";// 余额查询  
	public final static String SCODE_ACCOUNTCONSUMS = "1601";// 账户转充 
	
	public static final String BatchNo = "000001";// 批次�?
	public static final String MsgType = "0200";//消息类型
	public static final String ProcessingCode = "310000";// 交易处理�?
	public static final String PosEntryModeCode = "051";// 服务点输入方式码
	public static final String CardSeqId = "000";// 卡片序列�?
	public static final String PosCondCode = "00";// 服务点条件码
	public static final String CurrcyCodeTrans = "156";// 交易货币代码
	public static final String SecRelatdCtrlInfo = "2600000000000000";// 安全控制信息
	public static final String EmvVal = "0000000000";//IC卡数据域
	
	public static final String Method_BindingCard="BindingCards";//中石�?绑卡
	public static final String Method_GetCardNoInfo="GetCardInfoByTypeSs";//中石�?获取卡简要信�?
	public static final String Method_GetOrderInfo="GetOrderInfos";//获得预充值信�?
	public static final String Method_CardSpareMoneyRecharge="CardSpareMoneyRecharges";//获得预充值信�?
	public static final String Method_OrederQuery="OrderQuerys";//查询充�?状�?接口
	public static final String Method_ReconQuery="ReconQuerys";//对账文件通知接口
	
}
