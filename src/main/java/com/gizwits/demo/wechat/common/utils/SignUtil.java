package com.gizwits.demo.wechat.common.utils;

import com.gizwits.demo.wechat.common.SysCons;
import com.gizwits.demo.wechat.common.beans.WeChatVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by matt on 16-5-16.
 */
@Component
public class SignUtil {

    @Autowired
    SysCons sysCons;


    public boolean checkSignature(WeChatVerify weChatVerify) {
        String signature = weChatVerify.getSignature();
        String timestamp = weChatVerify.getTimestamp();
        String nonce = weChatVerify.getNonce();
        String token = sysCons.TOKEN;

        String[] strs = {token, timestamp, nonce};
        // 字典排序
        Arrays.sort(strs);
        String result = "";
        for (String str : strs) {
            result += str;
        }
        return SHA1(result).equals(signature);
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0 ; i < length; ++i){
            int number = random.nextInt(62);//[0,62)

            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
