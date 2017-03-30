package com.example.tonyw.acgwarehouse.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {
    public HttpUtils() {
                 // TODO Auto-generated constructor stub
    }
    //直接返回Json数据
    public static String getJsonContent(String url_path) {
        try {
            URL url = new URL(url_path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            if (code == 200)
            {
                return changeInputStream(connection.getInputStream(),"UTF-8");
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return "";
    }
    //改变流输出方向
    private static String changeInputStream(InputStream inputStream,String encode)
    {
        // TODO Auto-generated method stub
        String jsonString = "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len;
        byte[] data = new byte[1024];
        try {
            while ((len = inputStream.read(data)) != -1)
            {
                outputStream.write(data, 0, len);
            }
            jsonString = new String(outputStream.toByteArray(),encode);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonString;
    }

    //检查当前网络状态
    public static boolean isNetworkConnected(Context context)
    {
        if(context!=null)
        {
            ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null)
            {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
    //获取图片
    public static Bitmap getHttpBitmap(String url){
        Log.d("getPic","Im in");
        Long startTime=System.currentTimeMillis();
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL=new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(1000);
            //这句可有可无，没有影响
            conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            int size=conn.getContentLength();
            Log.d("file size", String.valueOf(size));
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
            Log.d("getPic","Im out");
        }catch(Exception e){
            e.printStackTrace();
        }
        Long endTime=System.currentTimeMillis();
        Log.d("getHttpTime", String.valueOf(endTime-startTime));
        return bitmap;
    }
    //从客户端获取参数后返回Json数据
    public static String getJsonData(String path)
    {
        try {
            URL url=new URL(path);
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            InputStreamReader is=new InputStreamReader(urlConnection.getInputStream());
            BufferedReader buffer = new BufferedReader(is);
            String inputLine;
            String result="";
            while (((inputLine = buffer.readLine()) != null)){
                result += inputLine + "\n";
            }
            is.close();
            urlConnection.disconnect();
            return result;
        }catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    //通过Post上传数据
    public static String sendPostMessage(Map<String, String> params, String encode,String path) {
        StringBuilder stringBuilder = new StringBuilder();
        URL url= null;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    Log.d("key",entry.getKey());
                    Log.d("value",entry.getValue());
                    stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            try {
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setConnectTimeout(3000);
                urlConnection.setRequestMethod("POST"); // 以post请求方式提交
                urlConnection.setDoInput(true); // 读取数据
                urlConnection.setDoOutput(true); // 向服务器写数据
                // 获取上传信息的大小和长度
                Log.d("stringBuilder",stringBuilder.toString());
                byte[] myData = stringBuilder.toString().getBytes();
                // 设置请求体的类型是文本类型,表示当前提交的是文本数据
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(myData.length));
                // 获得输出流，向服务器输出内容
                OutputStream outputStream = urlConnection.getOutputStream();
                // 写入数据
                outputStream.write(myData, 0, myData.length);
                outputStream.close();
                // 获得服务器响应结果和状态码
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == 200) {
                    // 取回响应的结果
                    return changeInputStream(urlConnection.getInputStream(),encode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "";
    }
}
