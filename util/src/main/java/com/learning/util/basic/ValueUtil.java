package com.learning.util.basic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.learning.util.exception.HzbuviException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by taylor on 8/9/16.
 * twitter: @taylorwang789
 */
public class ValueUtil {

    public static String defaultSuccess = "success";

    public static boolean notEmpity(Object obj) {
        if ( null == obj) {
            return false;
        } else if ( obj.toString().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isEmpity(Object obj) {
        return !notEmpity(obj);
    }

    public static <T> T coalesce(T... args) {
        for (int i = 0; i < args.length ; i++) {
            if (notEmpity(args[i])) {
                return args[i];
            }
        }
        return (T) "";
    }

//    public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    public static Gson gson = new Gson();

    public static String toJson(Object obj){
        return gson.toJson(obj);
    }


    public static String toError(String code,String message){
        RestJson restJson = new RestJson();
        restJson.setCode(code);
        restJson.setData("");
        restJson.setMsg(message);
        return toJson(restJson);
    }

    public static String toJson(Object... objs ){
        RestJson restJson = new RestJson();
        restJson.setCode(defaultSuccess);
        restJson.setMsg("");
        Map<String,Object> map = new HashMap<>();
        boolean isOdd = true;
        String key = "";
        for (int i = 0; i < objs.length ; i++) {
            if (isOdd) {
                key = objs[i].toString();
                isOdd = false;
            } else {
                if ( notEmpity(objs[i]) ) {
                    map.put(key,objs[i]);
                } else {
                    map.put(key,new JsonObject());
                }
                isOdd = true;
            }
        }
        restJson.setData(map);
        return toJson(restJson);
    }

    public static String nullJson(){
        RestJson restJson = new RestJson();
        restJson.setCode(defaultSuccess);
        restJson.setMsg("");
        restJson.setData(new JsonObject());
        return toJson(restJson);
    }

    public static void verify(Object obj , String errorCode ) throws HzbuviException {
        if ( isEmpity(obj) ) {
            throw new HzbuviException(errorCode);
        }
    }

    public static void verify(boolean wanna ,boolean condition,String errorCode) throws HzbuviException {
        if (isEmpity(condition)) {
            throw new HzbuviException(errorCode);
        } else {
            if (wanna != condition) {
                throw new HzbuviException(errorCode);
            }
        }
    }

    private static JsonParser jsonParser = new JsonParser();

    public static String getFromJson(String json , Object... args){
        JsonObject origin = jsonParser.parse(json).getAsJsonObject() ;
        for (int i = 0; i < args.length; i++) {
            if ( (i+1) == args.length  ) {
                String returnString = origin.get(args[i].toString()).toString();
                if (returnString.startsWith("\"")) {
                    return returnString.substring(1,returnString.length()-1);
                } else {
                    return returnString;
                }
            } else {
                if (args[i].getClass().equals(Integer.class)) {
                    origin = origin.getAsJsonArray().get(Integer.valueOf(args[i].toString())).getAsJsonObject();
                }
                if (args[i].getClass().equals(String.class)) {
                    origin = origin.getAsJsonObject(args[i].toString());
                }
            }
        }
        return origin.toString();
    }
}
