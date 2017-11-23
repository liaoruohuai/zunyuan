package com.learning.util.encryption;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by taylor on 9/20/16.
 * twitter: @taylorwang789
 */
public class RSA {

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    public static final String CHARSET = "UTF8";

    public static final String PUBLICKEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMxPYtmYc3sfHLgjQyxrcohmeY" +
                    "ANRC6o7dJW/Vc1ZhPQ7UY9MlLBQddOj09foQpK9Tx/D7FOeI2/GWHx7C9weYbHsw" +
                    "ysKbJIAJMKL4Za6rzggU+JN5U7hybfuL0Ml+dy7lrWW285IiXd1kv9xWBjfwwkG2" +
                    "oIpmHNRZcytt7xfynwIDAQAB";
    public static final String PRIVATEKEY =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMzE9i2Zhzex8cuC" +
                    "NDLGtyiGZ5gA1ELqjt0lb9VzVmE9DtRj0yUsFB106PT1+hCkr1PH8PsU54jb8ZYf" +
                    "HsL3B5hsezDKwpskgAkwovhlrqvOCBT4k3lTuHJt+4vQyX53LuWtZbbzkiJd3WS/" +
                    "3FYGN/DCQbagimYc1FlzK23vF/KfAgMBAAECgYBmRUke5TsfmgRFvDYN1inMV7SN" +
                    "unxJH5VS5kvAs+ZEkpZBhcoDGszBGY7wKZpHjbjzgzzcakhpllBGxiw6+94SYfL4" +
                    "7YDj/1VrpG8Fnvpie4VlC/q+zGqLtRkViYph1RVY7mDDH7IbljKu5Gez5zGcv8F3" +
                    "hrJUJoU6U83xY95TAQJBAOsbo7yIfSi10aFReoQ7/p2JxOuIhOpNE8ra1noOugu5" +
                    "srAbYIAgEWlV4adsBpNbzM/4jbL22gRzGrk55dRjz2ECQQDe9ynxZl9tv26TFTjS" +
                    "BlPLiio/qZt6FV4zt8qPcuF0sW/m+Kx6uaaF++Gk2py6zVfmKVFoonKXgj5PwyCp" +
                    "/QH/AkEA6KEKnCu5G6T77VC/l0P9pUeCT0NfsK3fXaK3AUK9F4kAo3wTOfOBBubx" +
                    "RkV/JCN7PnTDUEcsZaa21HvTSxoPQQJBALSIutxswO4zJ8xOTbC5UNYo7GaZ5Xsn" +
                    "nBwSQCCAq476MRp61eWVNwGb3qvGG0uiAv8pC3LGvjRxUBTzcRpS828CQAtV+Y0b" +
                    "4TEawb/JBMHnY4Ibk3fg51MSZZKvtHT3vhBkpPUsKRdkDB6mZU3i/wdH+b9rOv5M" +
                    "16QtUeaZHwbvSv8=";


    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @return 签名值
     */
    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * RSA验签名检查
     *
     * @param content        待签名数据
     * @param sign           签名值
     * @param publicKey 支付宝公钥
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String publicKey ) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHARSET));
            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String encrypt(String content, String publicKey) {
//        PublicKey pubKey = readPublicKey(filename);
        try {
            PublicKey pubKey = getPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherData = cipher.doFinal(content.getBytes(CHARSET));
            return  Base64.encode(cipherData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密
     *
     * @param content    密文
     * @param privateKey 商户私钥
     * @return 解密后的字符串
     */
    public static String decrypt(String content, String privateKey) {
        try {
            PrivateKey prikey = getPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, prikey);
            byte[] data = cipher.doFinal(Base64.decode(content));
            return new String(data, CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    private static PublicKey getPublicKey(String key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decode(key);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        return pubKey;
    }



}
