package com.learning.util.comm;

/**
 *
 */

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 字符串工具类
 *
 * @author zhenhuawang
 */
public class StrKit {
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String DOT = ".";

    public static final String ISO_8859_1 = "ISO-8859-1";
    public static final String UTF_8 = "UTF-8";
    public static final String GBK = "GBK";

    /**
     * 首字母变小写
     */
    public static String firstCharToLowerCase(String str) {
        Character firstChar = str.charAt(0);
        String tail = str.substring(1);
        str = Character.toLowerCase(firstChar) + tail;
        return str;
    }

    /**
     * 首字母变大写
     */
    public static String firstCharToUpperCase(String str) {
        Character firstChar = str.charAt(0);
        String tail = str.substring(1);
        str = Character.toUpperCase(firstChar) + tail;
        return str;
    }

    /**
     * 字符串为 null 或者为 "" 时返回 true
     */
    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim()) ? true : false;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 所有字符串都不为空 或者"" 时返回true
     *
     * @param strs
     * @return
     */
    public static boolean notBlank(String... strs) {
        if (strs == null)
            return false;
        for (String str : strs)
            if (str == null || "".equals(str.trim()))
                return false;
        return true;
    }

    /**
     * 所有对象不为null ，返回true
     *
     * @param paras
     * @return
     */
    public static boolean notNull(Object... paras) {
        if (paras == null)
            return false;
        for (Object obj : paras)
            if (obj == null)
                return false;
        return true;
    }

    /**
     * @param str
     * @param remove
     * @return
     */
    public static String removeEnd(String str, String remove) {
        if (isBlank(str) || isBlank(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

    public static String defaultString(final String str) {
        return str == null ? EMPTY : str;
    }

    /**
     * 取卡号前四位和后四位，中间填四个*
     *
     * @return
     */
    public static String getFormatCardNo(String string) {
        String cardNo = string.trim();
        String lastNo = cardNo.substring(cardNo.length() - 4);
        String firstNo = cardNo.substring(0, 4);
        String formatCardNo = firstNo + "*****" + lastNo;
        return formatCardNo;
    }

    /**
     * 替换中间字符为*号
     *
     * @param oleStr   要替换的
     * @param firstLen
     * @param lastLen
     * @return
     */
    public static String replaceStr(String oleStr, int firstLen, int lastLen) {
        if (isBlank(oleStr)) {
            return "";
        }
        String newStr = oleStr.trim();
        int len = newStr.length() - firstLen - lastLen;
        if (len <= 0) {
            return oleStr;
        }
        StringBuffer sb = new StringBuffer();
        String lastStr = newStr.substring(newStr.length() - lastLen);
        sb.append(newStr.substring(0, firstLen));
        for (int i = 0; i < len; i++) {
            sb.append("*");
        }
        sb.append(lastStr);
        return sb.toString();
    }

    public static int convertToInt(String intStr, int defValue) {
        if (isBlank(intStr)) {
            return defValue;
        }
        int i = defValue;
        try {
            i = Integer.parseInt(intStr);
        } catch (Exception e) {
            i = defValue;
        }
        return i;
    }

    /**
     * 二行制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + "";
        }
        return hs.toUpperCase();
    }

    /**
     * 填充字符串
     *
     * @param string
     * @param filler
     * @param totalLength
     * @param atEnd
     * @param encoding
     * @return
     */
    public static String fillStr(String string, char filler, int totalLength, boolean atEnd, String encoding) {
        byte[] tempbyte = null;
        try {
            tempbyte = string.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("fillStr get bytes error.", e);
        }

        int currentLength = tempbyte.length;
        int delta = totalLength - currentLength;
        for (int i = 0; i < delta; i++) {
            if (atEnd)
                string += filler;
            else
                string = filler + string;
        }
        return string;

    }

    /**
     * 填充字符串
     *
     * @param string
     * @param filler
     * @param totalLength
     * @param atEnd
     * @return
     */
    public static String fillStr(String string, char filler, int totalLength, boolean atEnd) {
        byte[] tempbyte = string.getBytes();
        int currentLength = tempbyte.length;
        int delta = totalLength - currentLength;
        for (int i = 0; i < delta; i++) {
            if (atEnd)
                string += filler;
            else
                string = filler + string;
        }
        return string;

    }

    /**
     * 转换字符串的字符集编码
     *
     * @param source     字符串
     * @param srcCharset 源字符集，默认ISO-8859-1
     * @param newCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    public static String convertCharset(String source, String srcCharset, String newCharset) {
        if (StrKit.isBlank(srcCharset)) {
            srcCharset = ISO_8859_1;
        }

        if (StrKit.isBlank(newCharset)) {
            srcCharset = UTF_8;
        }

        if (StrKit.isBlank(source) || srcCharset.equals(newCharset)) {
            return source;
        }
        try {
            return new String(source.getBytes(srcCharset), newCharset);
        } catch (UnsupportedEncodingException unex) {
            throw new RuntimeException(unex);
        }
    }

    public static String convertCharset(String source, String newCharset) {
        if (StrKit.isBlank(source)) {
            return source;
        }
        try {
            return new String(source.getBytes(), newCharset);
        } catch (UnsupportedEncodingException unex) {
            throw new RuntimeException(unex);
        }
    }

    /**
     * 编码字符串
     *
     * @param str     字符串
     * @param charset 字符集
     * @return 编码后的字节码
     */
    public static byte[] encode(String str, String charset) {
        if (str == null) {
            return null;
        }

        try {
            return str.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(format("Charset [{}] unsupported!", charset));
        }
    }

    /**
     * 解码字节码
     *
     * @param data    字符串
     * @param charset 字符集
     * @return 解码后的字符串
     */
    public static String decode(byte[] data, String charset) {
        if (data == null) {
            return null;
        }

        try {
            return new String(data, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(format("Charset [{}] unsupported!", charset));
        }
    }

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values   参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... values) {
        if (!notNull(values) || isBlank(template)) {
            return template;
        }

        final StringBuilder sb = new StringBuilder();
        final int length = template.length();

        int valueIndex = 0;
        char currentChar;
        for (int i = 0; i < length; i++) {
            if (valueIndex >= values.length) {
                sb.append(sub(template, i, length));
                break;
            }

            currentChar = template.charAt(i);
            if (currentChar == '{') {
                final char nextChar = template.charAt(++i);
                if (nextChar == '}') {
                    sb.append(values[valueIndex++]);
                } else {
                    sb.append('{').append(nextChar);
                }
            } else {
                sb.append(currentChar);
            }

        }

        return sb.toString();
    }

    /**
     * 格式化文本
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param map      参数值对
     * @return 格式化后的文本
     */
    public static String format(String template, Map<?, ?> map) {
        if (null == map || map.isEmpty()) {
            return template;
        }

        for (Entry<?, ?> entry : map.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString());
        }
        return template;
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" example: abcdefgh 2 3 -> c abcdefgh 2 -3 -> cde
     *
     * @param string    String
     * @param fromIndex 开始的index（包括）
     * @param toIndex   结束的index（不包括）
     * @return 字串
     */
    public static String sub(String string, int fromIndex, int toIndex) {
        int len = string.length();

        if (fromIndex < 0) {
            fromIndex = len + fromIndex;

            if (toIndex == 0) {
                toIndex = len;
            }
        }

        if (toIndex < 0) {
            toIndex = len + toIndex;
        }

        if (toIndex < fromIndex) {
            int tmp = fromIndex;
            fromIndex = toIndex;
            toIndex = tmp;
        }

        if (fromIndex == toIndex) {
            return EMPTY;
        }

        char[] strArray = string.toCharArray();
        char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
        return new String(newStrArray);
    }

    /**
     * 包装指定字符串
     *
     * @param str    被包装的字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 包装后的字符串
     */
    public static String wrap(String str, String prefix, String suffix) {
        return format("{}{}{}", getNotNull(prefix), getNotNull(str), getNotNull(suffix));
    }

    public static String getNotNull(String str) {
        return str == null ? "" : str;
    }


    /**
     * 生成get方法名<br/>
     *
     * @param fieldName
     * @return
     */
    public static String genGetter(String fieldName) {
        if (isBlank(fieldName)) {
            return null;
        }

        return "get" + firstCharToUpperCase(fieldName);
    }

    /**
     * 生成set方法名<br/>
     * 例如：name 返回 setName
     *
     * @param fieldName 属性名
     * @return setXxx
     */
    public static String genSetter(String fieldName) {
        if (isBlank(fieldName)) {
            return null;
        }

        return "set" + firstCharToUpperCase(fieldName);
    }

    /**
     * 去掉指定前缀
     *
     * @param str    字符串
     * @param prefix 前缀
     * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
     */
    public static String removePrefix(String str, String prefix) {
        if (str != null && str.startsWith(prefix)) {
            return str.substring(prefix.length());
        }
        return str;
    }
}
