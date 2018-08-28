package com.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/7/8.
 */
public class Base64Util {


    public static String decrypt(String message, String key) throws Exception {

        byte[] bytesrc = convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    public static byte[] encrypt(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return cipher.doFinal(message.getBytes("UTF-8"));
    }

    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }

        return digest;
    }

    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

    /**
     * 加密
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String EncryptAsDoNet(String message, String key) throws Exception {
        String jiami= URLEncoder.encode(message, "utf-8");
        String a=toHexString(encrypt(jiami, key));
        byte[] result = Base64.encodeBase64(a.getBytes());
        return new String(result);
    }


    /**
     * 解密
     * @param message
     * @param key
     * @return
     * @throws Exception
     */
    public static String DecryptDoNet(String message, String key) throws Exception {
        Base64 decoder = new Base64();
        byte[] bytesrc = decoder.decode(message);
        String b = new String(bytesrc);
        return java.net.URLDecoder.decode(decrypt(b,key), "utf-8");
    }



    public static String EncoderByMd5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
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
            return null;
        }
    }


    public static String getLowerField(String prefix,String field){
        StringBuffer sb = new StringBuffer();
        if(!StringUtils.isEmpty(prefix)){
            sb.append(prefix);
        }
        for(int i=0;i<field.length();i++){
            char tmp = field.charAt(i);
            if(Character.isUpperCase(tmp)){
                sb.append("_"+Character.toLowerCase(tmp));
            }else{
                sb.append(tmp);
            }
        }
        return sb.toString();
    }

    public static String getCode(){
        int code = (int)((Math.random()*9+1)*100000);
        return code+"";
    }
}
