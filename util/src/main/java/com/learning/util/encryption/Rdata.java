package com.learning.util.encryption;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by WANG, RUIQING on 9/27/16.
 * twitter: @taylorwang789
 * e-mail: i@wrqzn.com
 */
public class Rdata {
    private static Random r = new Random();

    private static Random rm = new Random();

    private static char randomChar(int from , int range) {
        char random_3_Char = (char) (from + r.nextInt(range));
        return random_3_Char;
    }

    public static char randomUperCase(){
        return randomChar(65,26);
    }

    public static String randomUperCase(int length){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append( randomChar(65,26) );
        }
        return builder.toString();
    }

    public static char randomLowerCase(){
        return randomChar(97,26);
    }

    public static String randomLowerCase(int length){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append( randomChar(97,26) );
        }
        return builder.toString();
    }

    public static char randomDigital(){
        return randomChar(48,10);
    }

    public static String randomDigital(int length){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append( randomChar(48,10) ) ;
        }
        return builder.toString();
    }

    public static String randomCode(int length){
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            switch (rm.nextInt(3)){
                case 0: builder.append( randomUperCase() ) ; break;
                case 1: builder.append( randomLowerCase() ) ; break;
                case 2: builder.append( randomDigital() ) ; break;
            }
        }
        return builder.toString();
    }

    public static String timeRandomCode(int length){
        return Base64.encode(longToBytes(System.currentTimeMillis())).substring(3,11)+randomCode(length);
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE/Byte.SIZE);
        buffer.putLong(x);
        return buffer.array();
    }
}
