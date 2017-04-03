package com.example.tonyw.acgwarehouse.activity;

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

import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.DEFAULT_SPAN_COUNT;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_FINISH;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.LOAD_MORE_DATA;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_DATA_GET;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_NETWORK;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.isNetworkConnected;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

public class ResourceActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private boolean isLoadingMore = true;
    private List<Entity> entityData=new ArrayList<>();
    private ResourceAdapter mResourceAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<VideoEntity> mPreVideoEntities=new ArrayList<>();
    private List<VideoEntity> mDownloadVideoEntities=new ArrayList<>();
    private List<VideoEntity> mLoadVideoEntities=new ArrayList<>();
    private JSONArray jsonArray=null;
    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case IS_FINISH:
                    setDynamicPreView(jsonArray);
                    setEntitiesData(mPreVideoEntities,mDownloadVideoEntities);
                    mResourceAdapter.notifyDataSetChanged();
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
                case LOAD_MORE_DATA:
                    Toast.makeText(getApplicationContext(),"载入新数据",Toast.LENGTH_SHORT).show();
                    for (int i=0;i<mLoadVideoEntities.size();i++)
                    {
                        Log.d("entity",mLoadVideoEntities.get(i).getVideoTitle());
                        mResourceAdapter.addItem(mLoadVideoEntities.get(i));
                        mPreVideoEntities.add(mLoadVideoEntities.get(i));
                    }
                    isLoadingMore=true;
                    mResourceAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.resource_recyclerview);
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),DEFAULT_SPAN_COUNT);
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = gridLayoutManager.getItemCount();
                Log.d("滑动中","ing");
                if (lastVisibleItem >= totalItemCount-10 && dy > 0) {
                    if(isLoadingMore){
                        isLoadingMore = false;
                        new Thread(new loadVideoInfo()).start();
                    }
                }
            }
        });
        Toolbar mToolbar;
        mToolbar= (Toolbar) findViewById(R.id.resource_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setDownloadData();
    }


    public void setDynamicPreView(JSONArray jsonArray)
    {
        VideoEntity mPreVideoEntity;
        for (int i=0;i<jsonArray.length();i++)
        {
            mPreVideoEntity=new VideoEntity();
            mResourceAdapter.addItem(mPreVideoEntity);
            mPreVideoEntities.add(mPreVideoEntity);
        }
    }
    //下载数据
    public void setDownloadData()
    {
        if(isNetworkConnected(getApplicationContext()))
        {
            new Thread(new downloadVideoInfo()).start();
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
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

    @Override
    public void onRefresh() {
        Log.d("Refresh","Running");
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //下载视频的相关数据
    private class downloadVideoInfo implements Runnable{
        @Override
        public void run() {
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/VideoDemo";
            String jsonString = HttpUtils.getJsonContent(path);
            try {
                List<Integer> randList;
                jsonArray=new JSONArray(jsonString);
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

    private class loadVideoInfo implements Runnable
    {
        @Override
        public void run() {
            try {
                List<Integer> randList;
                String path="http://tonywu10.imwork.net:16284/ACGWarehouse/VideoLoadDemo?lastVideo="+mPreVideoEntities.get(mPreVideoEntities.size()-1).getVideoUrl();
                JSONArray jsonArray;
                jsonArray = new JSONArray(getJsonData(path));
                randList=getSequence(jsonArray);
                mLoadVideoEntities.clear();
                setEntitiesDataFromJson(mLoadVideoEntities,jsonArray,randList);
                isLoadingMore=false;
                sendMessage(mHandler,LOAD_MORE_DATA);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //将预先加载的Entities进行数据填充
    public void setEntitiesData(List<VideoEntity> mPreEntities, List<VideoEntity> mEntities)
    {
        for (int i=0;i<mPreEntities.size();i++)
        {
            mPreEntities.get(i).setVideoThumbBitmap(mEntities.get(i).getVideoThumbBitmap());
            mPreEntities.get(i).setVideoTitle(mEntities.get(i).getVideoTitle());
            mPreEntities.get(i).setAllVideoEpisode(mEntities.get(i).getAllVideoEpisode());
            mPreEntities.get(i).setVideoUrl(mEntities.get(i).getVideoUrl());
            mPreEntities.get(i).setVideoIntro(mEntities.get(i).getVideoIntro());
        }
    }
    //将获得的Json数据填充
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
    //返回顺序序列
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
