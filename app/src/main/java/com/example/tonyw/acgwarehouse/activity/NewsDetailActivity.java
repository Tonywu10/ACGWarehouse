package com.example.tonyw.acgwarehouse.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.entity.UserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.FINISH_DATABASE;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_FINISH;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_PIC_FINISH;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

public class NewsDetailActivity extends AppCompatActivity{
    public String result="";
    public String resultFromDataBase="";
    private UserEntity mUserEntity;
    public List<Bitmap> newsPicBitmap=new ArrayList<>();
    public List<ImageView> imageViewList=new ArrayList<>();
    public LinearLayout mainLinearLayout;
    public Boolean isCollect;
    MenuItem collectItem;
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case IS_FINISH:
                    try {
                        mainLinearLayout= (LinearLayout) findViewById(R.id.news_detail_linearlayout);
                        JSONArray jsonArray=new JSONArray(result);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String newsContent=jsonObject.getString("NewsContent");
                            isCollect=jsonObject.getBoolean("IsCollect");
                            if(isCollect)
                            {
                                collectItem.setIcon(R.drawable.ic_star_black_24dp);
                            }
                            else
                            {
                                collectItem.setIcon(R.drawable.ic_star_border_black_24dp);
                            }
                            if(newsContent.startsWith("http"))
                            {
                                ImageView imageView=new ImageView(NewsDetailActivity.this);
                                LinearLayout.LayoutParams li=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                li.gravity= Gravity.CENTER;
                                li.topMargin=10;
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                imageView.setLayoutParams(li);
                                mainLinearLayout.addView(imageView,li);
                                imageViewList.add(imageView);
                            }
                            else
                            {
                                TextView textView=new TextView(NewsDetailActivity.this);
                                LinearLayout.LayoutParams li=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                li.topMargin=10;
                                textView.setLayoutParams(li);
                                textView.setText(newsContent);
                                textView.setTextSize(16);
                                mainLinearLayout.addView(textView,li);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case IS_PIC_FINISH:
                    for(int i=0;i<imageViewList.size();i++)
                    {
                        imageViewList.get(i).setImageBitmap(newsPicBitmap.get(i));
                    }
                    break;
                case FINISH_DATABASE:
                    try {
                        JSONArray jsonArray=new JSONArray(resultFromDataBase);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            if(jsonObject.getBoolean("finish"))
                            {
                                Toast.makeText(getApplicationContext(),"收藏成功",Toast.LENGTH_SHORT).show();
                                collectItem.setIcon(R.drawable.ic_star_black_24dp);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"取消收藏成功",Toast.LENGTH_SHORT).show();
                                collectItem.setIcon(R.drawable.ic_star_border_black_24dp);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.news_detail_toolbar);
        TextView mTextView= (TextView) findViewById(R.id.news_detail_title);
        Intent it=getIntent();
        mUserEntity= (UserEntity) getApplication();
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTextView.setText(it.getStringExtra("NewsTitle"));

        new getJson().start();
        new getNewsPic().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_detail_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.news_detail_collect:
                if(!mUserEntity.getUserName().equals(""))
                {
                    new collectOrNotCollectNews().start();
                }
                else
                {
                    Intent it=new Intent(NewsDetailActivity.this,LoginActivity.class);
                    startActivity(it);
                }
                break;
        }
        return true;
    }

    private class getJson extends Thread{
        @Override
        public void run() {
            Intent it=getIntent();
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsDetailDemo?news_url="+it.getStringExtra("news_url")+"&userName="+mUserEntity.getUserName();
            Log.d("path",path);
            try {
                result=getJsonData(path);
                sendMessage(mHandler,IS_FINISH);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class getNewsPic extends Thread{
        @Override
        public void run() {
            Intent it=getIntent();
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsDetailDemo?news_url="+it.getStringExtra("news_url")+"&userName="+mUserEntity.getUserName();
            try {
                result=getJsonData(path);
                JSONArray jsonArray=new JSONArray(result);
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String picString=jsonObject.getString("NewsContent");
                    if(picString.startsWith("http://www.gamersky.com"))
                    {
                        String realURL=picString.split("[?]")[1];
                        Bitmap newsBitmap=getHttpBitmap(realURL);
                        newsPicBitmap.add(newsBitmap);
                    }
                    else if(picString.startsWith("http"))
                    {
                        Bitmap newsBitmap=getHttpBitmap(picString);
                        newsPicBitmap.add(newsBitmap);
                    }
                }
                sendMessage(mHandler,IS_PIC_FINISH);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class collectOrNotCollectNews extends Thread
    {
        @Override
        public void run() {
            Intent it=getIntent();
            String name=mUserEntity.getUserName();
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsCollectStatusDemo?news_url="+it.getStringExtra("news_url")+"&userName="+name;
            resultFromDataBase=getJsonData(path);
            sendMessage(mHandler,FINISH_DATABASE);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        collectItem=menu.findItem(R.id.news_detail_collect);
        return true;
    }
}