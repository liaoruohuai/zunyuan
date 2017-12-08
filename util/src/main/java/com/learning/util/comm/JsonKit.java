package com.learning.util.comm;

/**
 *
 */

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * json 处理工具类
 *
 * @author wangzhenhua
 */
public class JsonKit {

    private static ObjectMapper objectMapper;

    /**
     * 把json字符串转换为相应的JavaBean对象。
     * 转换为普通JavaBean：readValue(json,User.class)
     * 转换为List:readValue(json,List.class).但是如果想把json转换为特定类型的List，比如List<User>，不能直接进行转换。
     * 因为readValue(json,List.class)返回的其实是List<Map>类型，不能指定readValue()的第二个参数是List<User>.class。
     * 可以把readValue()的第二个参数传递为User[].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List。
     * 转换为Map：readValue(json,Map.class)
     *
     * @param content   原始json字符串数据
     * @param valueType 要转换的JavaBean类型
     * @return JavaBean对象
     */
    public static <T> T readValue(String content, Class<T> valueType) {
        initMapper();
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 把json字符串转换为相应的List JavaBean对象。
     *
     * @param content
     * @param valueType
     * @return
     */
    public static <T> List<T> readListValue(String content, Class<T> valueType) {
        initMapper();
        TypeFactory t = TypeFactory.defaultInstance();
        List<T> list = null;
        try {
            list = objectMapper.readValue(content, t.constructCollectionType(ArrayList.class, valueType));
        } catch (Exception e) {
            throw new RuntimeException("unable to read object from result set", e);
        }
        return list;
    }

    /**
     * 把JavaBean转换为json字符串
     * 普通对象转换：toJson(user)
     * List转换：toJson(List)
     * Map转换:toJson(Map)
     * 不管什么类型，都可以直接传入这个方法
     *
     * @param object JavaBean对象
     * @return json字符串
     */
    public static String toJSon(Object object) {
        initMapper();

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void initMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            //当反序列化时忽略多余的属性
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            //Include.NON_NULL 属性为NULL 不序列化
            objectMapper.setSerializationInclusion(Include.NON_NULL);
        }
    }

    public static void main(String[] args) {

    }
}
