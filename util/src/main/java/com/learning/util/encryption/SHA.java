package com.learning.util.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by WANG, RUIQING on 9/27/16.
 * twitter: @taylorwang789
 * e-mail: i@wrqzn.com
 */
public class SHA {
    public static String e512(String str){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-512");
            return Base64.encode(digest.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
