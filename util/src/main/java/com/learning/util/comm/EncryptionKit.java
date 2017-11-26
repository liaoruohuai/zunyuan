package com.learning.util.comm;

import java.security.MessageDigest;

/**
 * MD5，SHA-1，SHA-256，SHA-384，SHA-512
 *
 * @author wangzhenhua
 *
 */
public class EncryptionKit {
    private static final String CHARSET = "UTF-8";

    public static String md5Encrypt(String srcStr) {
        return encrypt("MD5", srcStr);
    }

    public static String sha1Encrypt(String srcStr) {
        return encrypt("SHA-1", srcStr);
    }

    public static String sha256Encrypt(String srcStr) {
        return encrypt("SHA-256", srcStr);
    }

    public static String sha384Encrypt(String srcStr) {
        return encrypt("SHA-384", srcStr);
    }

    public static String sha512Encrypt(String srcStr) {
        return encrypt("SHA-512", srcStr);
    }

    public static String encrypt(String algorithm, String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes(CHARSET));
            return byte2hex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                result.append("0");
            }
            result.append(hex);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String str = "wb94mh36";
        System.out.println(md5Encrypt(str));
        System.out.println(sha1Encrypt(str));
        System.out.println(sha256Encrypt(str));
        System.out.println(sha384Encrypt(str));
        System.out.println(sha512Encrypt(str));
    }
}