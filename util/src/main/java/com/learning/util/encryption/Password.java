package com.learning.util.encryption;

/**
 * Created by WANG, RUIQING on 9/27/16.
 * twitter: @taylorwang789
 * e-mail: i@wrqzn.com
 */
public class Password {

    private static short[] param = {14,20,70};


    public static String encryption(String passwd){
        String firstHash = SHA.e512(passwd);
        String secondHash = SHA.e512(firstHash.substring(param[1],param[2])+firstHash);
        return RSA.encrypt(Rdata.randomCode(param[0])+secondHash.substring(param[1],param[2]), RSA.PUBLICKEY);
    }

    public static boolean verify(String input,String encrypedPasswd){
        String firstHash = SHA.e512(input);
        String secondHash = SHA.e512(firstHash.substring(param[1],param[2])+firstHash);
        if (secondHash.substring(param[1],param[2]).equals(RSA.decrypt(encrypedPasswd,RSA.PRIVATEKEY).substring(param[0]))) {
            return true;
        } else {
            return false;
        }
    }

}
