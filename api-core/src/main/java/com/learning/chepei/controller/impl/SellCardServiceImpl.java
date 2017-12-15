package com.learning.chepei.controller.impl;

import com.learning.chepei.DataConfig;
import com.learning.order.entity.Apply;
import com.learning.util.basic.Constants;
import com.learning.util.comm.RanKit;
import com.learning.util.date.DateUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Date;

public class SellCardServiceImpl {

    public static String getXMLReq(Apply apply) throws DocumentException {
        String message = Constants.messageP001;

        Document document = DocumentHelper.parseText(message);

        Element rootElement = document.getRootElement();
        Element baseElm=rootElement.element("BASE");
        Element dataElm=rootElement.element("DATA");
        Element macElm=rootElement.element("MAC");

        baseElm.element("TXN_CODE").setText("P001");
        baseElm.element("MSG_TYPE").setText("1");
        baseElm.element("TXN_TIME").setText(DateUtil.toString(new Date(),"yyyyMMddHHmmss"));
        baseElm.element("REQSEQNO").setText(apply.getApplyId());
        baseElm.element("RANDOM").setText(RanKit.getRAN(1,9));
        baseElm.element("CHANNEL").setText(DataConfig.ISSUECARD_CHANNEL);
        baseElm.element("ISSUER_ID").setText(DataConfig.ISSUECARD_ISSUER_ID);
        baseElm.element("TERM_ID").setText(DataConfig.ISSUECARD_TERM_ID);
        baseElm.element("BRANCH_ID").setText(DataConfig.ISSUECARD_BRANCH_ID);
        baseElm.element("MCHNT_CD").setText(DataConfig.ISSUECARD_MCHNT_CD);

        dataElm.element("ID_NO").setText(apply.getIdNum());
        dataElm.element("ID_TYPE").setText("1");
        dataElm.element("CARD_NO").setText(apply.getApplyCard());
        dataElm.element("FIRST_NAME").setText(apply.getName());
        dataElm.element("CARDHOLDER_MOBILE").setText(apply.getMobile());
        dataElm.element("CARDHOLDER_GENDER").setText(apply.getGender());
        dataElm.element("VALIDITY").setText(apply.getIdDate());

        macElm.setText("0000");

        return document.asXML();
    }

    public static String getXMLResp(String resp) throws DocumentException {
        Document document = DocumentHelper.parseText(resp);

        System.out.println("售卡应答报文：+ [" + document.asXML() + "]");
        return document.getRootElement().element("BASE").element("RESP_CODE").getText();
    }
}
