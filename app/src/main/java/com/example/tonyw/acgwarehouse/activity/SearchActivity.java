package com.example.tonyw.acgwarehouse.activity;

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

import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.DEFAULT_SPAN_COUNT;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_FINISH;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_DATA_GET;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_NETWORK;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_RESULT;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.isNetworkConnected;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

public class SearchActivity extends AppCompatActivity{
    private List<Entity> entityData=new ArrayList<>();
    private SearchAdapter mSearchAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String path="";
    private List<VideoEntity> mPreVideoEntities=new ArrayList<>();
    private List<VideoEntity> mDownloadVideoEntities=new ArrayList<>();
    private JSONArray jsonArray;
    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case IS_FINISH:
                    setDynamicPreView(jsonArray);
                    setEntitiesData(mPreVideoEntities,mDownloadVideoEntities);
                    mSearchAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_NETWORK:
                    Toast.makeText(getApplicationContext(),"网络出错",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_DATA_GET:
                    Toast.makeText(getApplicationContext(),"无法获得数据，请刷新",Toast.LENGTH_SHORT).show();
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
        RecyclerView mRecyclerView;
        mRecyclerView= (RecyclerView)findViewById(R.id.search_recyclerview);
        Intent it=getIntent();
        path="http://tonywu10.imwork.net:16284/ACGWarehouse/SearchDemo?videoName="+it.getStringExtra("videoName");
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mSearchAdapter =new SearchAdapter(entityData,DEFAULT_SPAN_COUNT,this);
        mRecyclerView.setAdapter(mSearchAdapter);
        mSwipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.search_swipeRefresh);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        setDownloadData();
        Toolbar mToolbar;
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
        VideoEntity mPreVideoEntity;
        for (int i=0;i<jsonArray.length();i++)
        {
            mPreVideoEntity=new VideoEntity();
            mSearchAdapter.addItem(mPreVideoEntity);
            mPreVideoEntities.add(mPreVideoEntity);
        }
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

    private class downloadVideoInfo implements Runnable{
        @Override
        public void run() {
            String jsonString = getJsonData(path);
            try {
                List<Integer> randList;
                jsonArray=new JSONArray(jsonString);
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

    public void setEntitiesDataFromJson(List<VideoEntity> mEntities,JSONArray jsonArray,List<Integer> randList) throws JSONException {
        VideoEntity mRefreshVideoEntity;
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
