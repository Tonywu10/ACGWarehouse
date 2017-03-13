package com.example.tonyw.acgwarehouse.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.ResourceAdapter;
import com.example.tonyw.acgwarehouse.entity.VideoEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;
import com.example.tonyw.acgwarehouse.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_FINISH;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_DATA_GET;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_NETWORK;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.REFRESH_COMPLETE;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.isNetworkConnected;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class ResourceActivity extends AppCompatActivity{
    private Toolbar mToolbar;
    public static final int DEFAULT_SPAN_COUNT=2;
    public static Activity resourceActivity;
    private RecyclerView mRecyclerView;
    private List<Entity> entityData=new ArrayList<>();
    private ResourceAdapter mResourceAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String jsonString="";
    private String path="http://tonywu10.imwork.net:16284/ACGWarehouse/VideoDemo";
    private List<VideoEntity> mPreVideoEntities=new ArrayList<>();
    private VideoEntity mPreVideoEntity;
    private List<VideoEntity> mDownloadVideoEntities=new ArrayList<>();
    private List<VideoEntity> mRefreshVideoEntities=new ArrayList<>();
    private VideoEntity mRefreshVideoEntity;
    private JSONArray jsonArray=null;
    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    setEntitiesData(mPreVideoEntities,mRefreshVideoEntities);
                    mResourceAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case IS_FINISH:
                    setDynamicPreView(jsonArray);
                    Long startTime=System.currentTimeMillis();
                    setEntitiesData(mPreVideoEntities,mDownloadVideoEntities);
                    Long endTime=System.currentTimeMillis();
                    Log.d("finish time", String.valueOf(endTime-startTime));
                    mResourceAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_NETWORK:
                    Log.d("no_network","I'm in");
                    Toast.makeText(getApplicationContext(),"network is down",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_DATA_GET:
                    Log.d("no_data_get","I'm in");
                    Toast.makeText(getApplicationContext(),"no data,please refresh!",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        resourceActivity=this;
        mRecyclerView= (RecyclerView)findViewById(R.id.resource_recyclerview);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mResourceAdapter =new ResourceAdapter(entityData,DEFAULT_SPAN_COUNT,this);
        mRecyclerView.setAdapter(mResourceAdapter);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.resource_swipeRefresh);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        setDownloadData();
        mToolbar= (Toolbar) findViewById(R.id.resource_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setDynamicPreView(JSONArray jsonArray)
    {
        initDynamicPreViewData(jsonArray);
    }

    public void setDownloadData()
    {
        if(isNetworkConnected(getApplicationContext()))
        {
            Log.d("start downloading","hello");
            new Thread(new downloadVideoInfo()).start();
        }
        else
        {
            Log.d("TAG","thread in");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("Thread","I'm in");
                    try {
                        Thread.sleep(1000);
                        sendMessage(mHandler,NO_NETWORK);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public class downloadVideoInfo implements Runnable{
        @Override
        public void run() {
            jsonString= HttpUtils.getJsonContent(path);
            Log.d("TAG",jsonString);
            try {
                List<Integer> randList;
                jsonArray=new JSONArray(jsonString);
                Log.d("jsonArray长度", String.valueOf(jsonArray.length()));
                randList=getSequence(jsonArray);
                setEntitiesDataFromJson(mDownloadVideoEntities,jsonArray,randList);
                sendMessage(mHandler,IS_FINISH);
            } catch (JSONException e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            sendMessage(mHandler,NO_DATA_GET);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        }
    }

    public void setEntitiesData(List<VideoEntity> mPreEntities, List<VideoEntity> mEntities)
    {
        for (int i=0;i<mPreEntities.size();i++)
        {
            Log.d("TAG", String.valueOf(mPreEntities.size()));
            mPreEntities.get(i).setVideoThumbBitmap(mEntities.get(i).getVideoThumbBitmap());
            mPreEntities.get(i).setVideoTitle(mEntities.get(i).getVideoTitle());
            mPreEntities.get(i).setAllVideoEpisode(mEntities.get(i).getAllVideoEpisode());
            mPreEntities.get(i).setVideoUrl(mEntities.get(i).getVideoUrl());
            mPreEntities.get(i).setVideoIntro(mEntities.get(i).getVideoIntro());
        }
    }

    public void initDynamicPreViewData(JSONArray jsonArray)
    {
        for (int i=0;i<jsonArray.length();i++)
        {
            mPreVideoEntity=new VideoEntity();
            mResourceAdapter.addItem(mPreVideoEntity);
            mPreVideoEntities.add(mPreVideoEntity);
        }
    }

    public void setEntitiesDataFromJson(List<VideoEntity> mEntities,JSONArray jsonArray,List<Integer> randList) throws JSONException {
        for (int i=0;i<randList.size();i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(randList.get(i));
            mRefreshVideoEntity=new VideoEntity();
            mRefreshVideoEntity.setVideoThumbBitmap(getHttpBitmap(jsonObject.getString("ThumbPath")));
            mRefreshVideoEntity.setVideoTitle(jsonObject.getString("VideoTitle"));
            mRefreshVideoEntity.setVideoUrl(jsonObject.getString("VideoUrl"));
            mRefreshVideoEntity.setAllVideoEpisode(jsonObject.getInt("AllEpisode"));
            mRefreshVideoEntity.setVideoIntro(jsonObject.getString("VideoIntro"));
            mEntities.add(mRefreshVideoEntity);
        }
    }

    public List<Integer> getSequence(JSONArray jsonArray)
    {
        List<Integer> seqList=new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++)
        {
            seqList.add(i);
        }
        return seqList;
    }
}
