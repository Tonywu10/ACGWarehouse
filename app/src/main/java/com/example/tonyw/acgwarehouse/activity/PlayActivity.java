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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.utils.MyMediaController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.BATTERY;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_FINISH;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.TIME;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

public class PlayActivity extends AppCompatActivity implements Runnable{
    private VideoView mVideoView;
    private MyMediaController myMediaController;
    private String videoId="";
    private String result="";
    private Bundle mBundle = new Bundle();

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

    protected void hideBottomUIMenu() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        hideBottomUIMenu();
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window window = PlayActivity.this.getWindow();
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_play);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        Intent it=getIntent();
        videoId = it.getStringExtra("videoId");
        Log.d("avId",videoId);
        new getPlayUrl().start();
        MediaController mMediaController;
        mMediaController = new MediaController(this);
        myMediaController = new MyMediaController(this, mVideoView, this);
        mMediaController.show(5000);
        mVideoView.setMediaController(myMediaController);
        mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);//高画质
        mVideoView.requestFocus();
        registerBroadcastReceiver();
        new Thread(this).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Log.d("-----------------------------------","onDestroy--------------------------------------");
            unregisterReceiver(batteryBroadcastReceiver);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    private BroadcastReceiver batteryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
                int level = intent.getIntExtra("level", 0);
                int scale = intent.getIntExtra("scale", 100);
                Message msg = new Message();
                msg.obj = (level*100)/scale+"";
                msg.what = BATTERY;
                mHandler.sendMessage(msg);
            }
        }
    };

    public void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryBroadcastReceiver, intentFilter);
    }

    @Override
    public void run() {
        while (true) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINESE);
            String str = sdf.format(new Date());
            Long playDuration = mVideoView.getCurrentPosition();
            if(playDuration!=0){
                mBundle.putLong("playDuration",playDuration);
                Log.d("-----------------------------duration-----------------------------", String.valueOf(playDuration));
            }
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

    private class getPlayUrl extends Thread{
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("-----------------------------------","onStop--------------------------------------");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("-----------------------------------","onResume--------------------------------------");
        mVideoView.seekTo(mBundle.getLong("playDuration"));
    }
}
