package com.mk.convention.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密类（封装jdk自带的md5加密方法）
 *
 * @author fengshuonan
 * @date 2016年12月2日 下午4:14:22
 */
public class MD5Util {
    private static final Logger LOGGER = LoggerFactory.getLogger(MD5Util.class);

    private static final String CHARSET = "UTF-8";


    public static String encrypt(String source) {
        return encodeMd5(source.getBytes());
    }

    private static String encodeMd5(byte[] source) {
        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(source));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10)
                buffer.append("0");
            buffer.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buffer.toString();
    }
    /**
     * 32位小写MD5
     *
     * @param str
     * @return
     */
    public static String parseStrToMd5L32(String str) {

        if (null == str) {
            LOGGER.warn(".parseStrToMd5L32() str is null.");
            return str;
        }

        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b: bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(bt));
            }

            reStr = sb.toString();

            return reStr;

        } catch (Exception e) {
            LOGGER.error(".parseStrToMd5L32() Exception={},param={}", e, str,
                    e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 32位大写MD5
     *
     * @param str
     * @return
     */
    public static String parseStrToMd5U32(String str) {
        String reStr = parseStrToMd5L32(str);
        if (null != reStr) {
            reStr = reStr.toUpperCase();
        }
        return reStr;
    }


    /**
     *
     * @dateTime 2017年4月14日 下午4:01:54
     * @author 陈麟  chenlin@lovego.com
     * <p>MD5加密</p>
     * @param s 需加密的字符串
     * @return 加密后的字符串结果
     */
    public final static String encode(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes(CHARSET);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     *
     * @dateTime 2017年4月14日 下午4:01:54
     * @author 陈麟  chenlin@lovego.com
     * <p>MD5加密</p>
     * @param s 需加密的字符串
     * @param encode 加密的encode类型
     * @return 加密后的字符串结果
     */
    public final static String encode(String s, String encode){
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes(encode);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            return null;
        }
    }

    
    public static void main(String[] args) {
        System.out.println(parseStrToMd5L32("乐富购API"));
        System.out.println(parseStrToMd5U32("yourclientsecret2014-01-01 01:01:01yourclientidyourpinyourpasswordaccess_tokenyourclientsecret"));
//        002b563150a6873f95079b1ab4d5a91e
//        372b60b5b473aa224d8b1134602cad73
    }
}
