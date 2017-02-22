package com.example.tonyw.acgwarehouse.utils;

import android.os.Handler;
import android.os.Message;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tonyw on 2017/2/15.
 */

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
            StringBuffer strBuf=new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
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
