package com.learning.util.comm;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 随机数工具类
 *
 * @author liaoruohuai
 */
public class RanKit {
    /**
     * 得到10位以内的随机数字符串
     *
     * @param beginInt
     * @param endInt
     * @return
     */
    public static String getRAN(int beginInt, int endInt) {
        String[] beforeShuffle = new String[] { "1","2", "3", "4", "5", "6", "7","8", "9", "0" };
        List list = Arrays.asList(beforeShuffle); //将数组转成List
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(beginInt, endInt);
        return result;
    }
}
