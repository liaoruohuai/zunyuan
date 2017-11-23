package com.learning.util.basic;


import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by taylor on 7/5/16.
 */
public class MapUtil {

    public static Map<String,Object> toMap(Object... objs){
        Map<String,Object> map = new HashMap<>();
        boolean isOdd = true;
        String key = "";
        for (int i = 0; i < objs.length ; i++) {
            if (isOdd) {
                key = objs[i].toString();
                isOdd = false;
            } else {
                if ( ValueUtil.notEmpity(objs[i]) ) {
                    map.put(key,objs[i]);
                } else {
                    map.put(key,new JsonObject());
                }
                isOdd = true;
            }
        }
        return map;
    }

    public static Map<String,Object> objectToMap(Object entity){
        Map<String,Object> map = new HashMap<>();
        if(null == entity){
            return null;
        }
        Field[] fields = entity.getClass().getDeclaredFields();
        for (int i=0;i<fields.length;i++){
            Field field = fields[i];
            field.setAccessible(true);
            try {
                map.put(field.getName(),field.get(entity) );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    public static Object mapToObject(Map<String,Object> map , Class classs) {
        if(null == map){
            return null;
        }
        Field[] fields = classs.getDeclaredFields();
        try {
            Object  obj = classs.newInstance();

            for (int i=0;i<fields.length;i++){
                Field field = fields[i];
                String setterName="set"+field.getName().substring(0,1).toUpperCase()+field.getName().substring(1);
                Method setter = null;
                Class fieldType = field.getType();

                setter = classs.getMethod(setterName,fieldType);
                switch (String.valueOf(fieldType)){
                    case "class java.lang.String":
                        setter.invoke(obj,String.valueOf(map.get(field.getName())));
                        break;
                    case "class java.lang.Integer":
                        setter.invoke(obj,Integer.valueOf( String.valueOf(map.get(field.getName()) )   ));
                        break;
                    case "class java.lang.Boolean":
                        setter.invoke(obj, Boolean.valueOf( String.valueOf(map.get(field.getName()) )   ));
                        break;
                    case "class java.lang.Long":
                        setter.invoke(obj, Long.valueOf( String.valueOf(map.get(field.getName()) )   ));
                        break;
                    case "class java.lang.Double":
                        setter.invoke(obj, Double.valueOf( String.valueOf(map.get(field.getName()) )   ));
                        break;
                    case "class java.lang.Float":
                        setter.invoke(obj, Float.valueOf( String.valueOf(map.get(field.getName()) )   ));
                        break;
                    case "class java.math.BigInteger":
                        setter.invoke(obj, BigInteger.valueOf( Long.parseLong(String.valueOf(map.get(field.getName())))));
                        break;
                    case "class java.math.BigDecimal":
                        setter.invoke(obj, BigDecimal.valueOf( Long.parseLong( String.valueOf(map.get(field.getName())))));
                        break;
//                    case "class java.util.Date":
//                        setter.invoke(obj, java.util.Date.valueOf( String.valueOf(map.get(field.getName()))));
//                        break;
                    case "class java.sql.Date":
                        setter.invoke(obj, Date.valueOf( String.valueOf(map.get(field.getName()))));
                        break;
                    case "class java.sql.Time":
                        setter.invoke(obj, Time.valueOf(String.valueOf(map.get(field.getName()))));
                        break;
                    case "class java.sql.Timestamp":
                        setter.invoke(obj, Timestamp.valueOf( String.valueOf(map.get(field.getName()))));
                        break;
                    default:
                        System.out.println();
                }
            }
            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public static Map<String,String> getParameter(Map<String,String[]> maps ){
        Map<String,String> out = new HashMap<>();
        for (Map.Entry<String,String[]> set: maps.entrySet() ) {
            out.put(set.getKey(),set.getValue()[0]);
        }
        return out;
    }

}
