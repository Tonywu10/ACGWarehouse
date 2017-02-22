package com.example.tonyw.acgwarehouse.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.example.tonyw.acgwarehouse.adapters.SearchAdapter;
import com.example.tonyw.acgwarehouse.entity.VideoEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.isNetworkConnected;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;


/**
 * Created by tonywu10 on 2016/12/4.
 */

public class SearchActivity extends AppCompatActivity{
    private static final int NO_RESULT=100;
    private static final int IS_FINISH=101;
    private static final int NO_NETWORK=103;
    private static final int NO_DATA_GET=104;

    private Toolbar mToolbar;
    public static final int DEFAULT_SPAN_COUNT=2;
    public static Activity searchActivity;
    private RecyclerView mRecyclerView;
    private List<Entity> entityData=new ArrayList<>();
    private SearchAdapter mSearchAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String jsonString="";
    private String path="";
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
                case IS_FINISH:
                    setDynamicPreView(jsonArray);
                    Long startTime=System.currentTimeMillis();
                    setEntitiesData(mPreVideoEntities,mDownloadVideoEntities);
                    Long endTime=System.currentTimeMillis();
                    Log.d("finish time", String.valueOf(endTime-startTime));
                    mSearchAdapter.notifyDataSetChanged();
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
                case NO_RESULT:
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(),"没有匹配的结果",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchActivity=this;
        mRecyclerView= (RecyclerView)findViewById(R.id.search_recyclerview);
        Intent it=getIntent();
        Log.d("videoName",it.getStringExtra("videoName"));
        path="http://tonywu10.imwork.net:16284/ACGWarehouse/SearchDemo?videoName="+it.getStringExtra("videoName");
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mSearchAdapter =new SearchAdapter(entityData,gridLayoutManager,DEFAULT_SPAN_COUNT);
        mRecyclerView.setAdapter(mSearchAdapter);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.search_swipeRefresh);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        setDownloadData();
        mToolbar= (Toolbar) findViewById(R.id.search_toolbar);
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
            jsonString= getJsonData(path);
            Log.d("TAG",jsonString);
            try {
                List<Integer> randList;
                jsonArray=new JSONArray(jsonString);
                Log.d("jsonArray长度", String.valueOf(jsonArray.length()));
                randList=getSequence(jsonArray);
                if(jsonArray.getJSONObject(0).getBoolean("Exist"))
                {
                    setEntitiesDataFromJson(mDownloadVideoEntities,jsonArray,randList);
                    sendMessage(mHandler,IS_FINISH);
                }
                else
                {
                    sendMessage(mHandler,NO_RESULT);
                }
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
            mSearchAdapter.addItem(mPreVideoEntity);
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
