package com.example.tonyw.acgwarehouse.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpUtils {
    public HttpUtils() {
                 // TODO Auto-generated constructor stub
    }

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
                return changeInputStream(connection.getInputStream());
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return "";
    }

    private static String changeInputStream(InputStream inputStream)
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
            jsonString = new String(outputStream.toByteArray());
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

    public static String getJsonData(String path)
    {
        try {
            URL url;
            url=new URL(path);
            HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            InputStreamReader is=new InputStreamReader(urlConnection.getInputStream());
            BufferedReader buffer = new BufferedReader(is);
            String inputLine;
            String result="";
            while (((inputLine = buffer.readLine()) != null)){
                Log.d("Running","Im in");
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

    public static void sendUserData(String path,String attr1,String attr2,String attr3)
    {
        ArrayList<NameValuePair> data=new ArrayList<>();
        data.add(new BasicNameValuePair("userName",attr1));
        data.add(new BasicNameValuePair("userPassword",attr2));
        data.add(new BasicNameValuePair("userAvatar",attr3));
        HttpClient client=new DefaultHttpClient();
        HttpPost post=new HttpPost(path);
        try {
            post.setEntity(new UrlEncodedFormEntity(data));
            client.execute(post);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
