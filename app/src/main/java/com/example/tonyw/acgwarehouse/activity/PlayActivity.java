package com.example.tonyw.acgwarehouse.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.utils.MyMediaController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

/**
 * Created by tonyw on 2016/12/28.
 */

public class PlayActivity extends AppCompatActivity implements Runnable{
    public static  final String TAG = "PlayActivity";
    private VideoView mVideoView;
    private MediaController mMediaController;
    private MyMediaController myMediaController;
    private String videoId="";
    private String result="";

    private static final int TIME = 0;
    private static final int BATTERY = 1;
    private static final int IS_FINISH = 2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME:
                    myMediaController.setTime(msg.obj.toString());
                    break;
                case BATTERY:
                    myMediaController.setBattery(msg.obj.toString());
                    break;
                case IS_FINISH:
                    try {
                        JSONArray jsonArray=new JSONArray(result);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String playUrl=jsonObject.getString("PlayUrl");
                            Log.d("PlayUrl",playUrl);
                            mVideoView.setVideoPath(playUrl);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = PlayActivity.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_play);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        Intent it=getIntent();
        videoId = it.getStringExtra("videoId");
        Log.d("avId",videoId);
        new getPlayUrl().start();
        mMediaController = new MediaController(this);
        myMediaController = new MyMediaController(this, mVideoView, this);
        mMediaController.show(5000);
        mVideoView.setMediaController(myMediaController);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
        mVideoView.requestFocus();
        //画面是否拉伸
//        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 16/9 );
        registerBoradcastReceiver();
        new Thread(this).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(batteryBroadcastReceiver);
        } catch (IllegalArgumentException ex) {

        }
    }

    private BroadcastReceiver batteryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                //把它转成百分比
                //tv.setText("电池电量为"+((level*100)/scale)+"%");
                Message msg = new Message();
                msg.obj = (level*100)/scale+"";
                msg.what = BATTERY;
                mHandler.sendMessage(msg);
            }
        }
    };

    public void registerBoradcastReceiver() {
        //注册电量广播监听
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryBroadcastReceiver, intentFilter);

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            //时间读取线程
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String str = sdf.format(new Date());
            Message msg = new Message();
            msg.obj = str;
            msg.what = TIME;
            mHandler.sendMessage(msg);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public class getPlayUrl extends Thread{
        @Override
        public void run()
        {
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/VideoPlayUrlDemo?videoId="+videoId;
            android.util.Log.d("path",path);
            try {
                result=getJsonData(path);
                sendMessage(mHandler,IS_FINISH);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
