package com.learning.util.basic;

import com.learning.util.date.DateUtil;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * .维护每日重置的一个或多个序列号
 * Author: Liaoruohuai
 */
public class TxnSequence {

    private static String DateStamp ="";
    private static int SellCardID = 1;
    public TxnSequence(){
        String NowDate = DateUtil.toString(new Date(),"yyyyMMdd");
        if (NowDate.equals(DateStamp)){
            ++SellCardID;
        }else{
            SellCardID = 1;
            DateStamp = NowDate;
        }
    }
    public static String getSellCardID() {
        DecimalFormat decimalFormat = new DecimalFormat("00000000");
        return decimalFormat.format(SellCardID);
    }
}
