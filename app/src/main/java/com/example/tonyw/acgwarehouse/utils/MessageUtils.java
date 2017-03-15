package com.example.tonyw.acgwarehouse.utils;

import android.os.Handler;
import android.os.Message;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageUtils {
    public MessageUtils() {
    }
    public static void sendMessage(Handler mHandler,int message_type)
    {
        Message message=Message.obtain();
        message.what=message_type;
        mHandler.sendMessage(message);
    }

    public static String getPasswordMD5(String password){
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes("UTF-8"));
            byte[] encryption=md5.digest();
            StringBuilder strBuf=new StringBuilder();
            for (byte anEncryption : encryption) {
                if (Integer.toHexString(0xff & anEncryption).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & anEncryption));
                } else {
                    strBuf.append(Integer.toHexString(0xff & anEncryption));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";

        }
    }
}
