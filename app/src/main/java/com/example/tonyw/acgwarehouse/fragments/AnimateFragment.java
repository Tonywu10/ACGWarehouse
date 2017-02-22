package com.example.tonyw.acgwarehouse.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.AnimateAdapter;
import com.example.tonyw.acgwarehouse.entity.CategoryEntity;
import com.example.tonyw.acgwarehouse.entity.VideoEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;
import com.example.tonyw.acgwarehouse.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.isNetworkConnected;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

/**
 * Created by tonywu10 on 2016/11/28.
 */

public class AnimateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final int DEFAULT_SPAN_COUNT=2;
    private static final int REFRESH_COMPLETE=100;
    private static final int IS_FINISH=101;
    public static final int IS_CACHED=102;
    private static final int NO_NETWORK=103;
    private static final int NO_DATA_GET=104;

    private RecyclerView mRecyclerView;
    private AnimateAdapter mAnimateAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Entity> entityData=new ArrayList<>();
    private List<VideoEntity> mPreVideoEntities=new ArrayList<>();
    private VideoEntity mPreVideoEntity;
    private List<VideoEntity> mDownloadVideoEntities=new ArrayList<>();
    private List<VideoEntity> mRefreshVideoEntities=new ArrayList<>();
    private VideoEntity mRefreshVideoEntity;

    private String jsonString;
    private String path="http://tonywu10.imwork.net:16284/ACGWarehouse/VideoDemo";

    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    setEntitiesData(mPreVideoEntities,mRefreshVideoEntities);
                    mAnimateAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case IS_FINISH:
                    setDynamicPreView();
                    Long startTime=System.currentTimeMillis();
                    setEntitiesData(mPreVideoEntities,mDownloadVideoEntities);
                    Long endTime=System.currentTimeMillis();
                    Log.d("finish time", String.valueOf(endTime-startTime));
                    mAnimateAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case IS_CACHED:
                    mAnimateAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_NETWORK:
                    Log.d("no_network","I'm in");
                    Toast.makeText(getActivity().getApplicationContext(),"network is down",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_DATA_GET:
                    Log.d("no_data_get","I'm in");
                    Toast.makeText(getActivity().getApplicationContext(),"no data,please refresh!",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_animate, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.animate_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.animate_swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });

        setDownloadData();
        final GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAnimateAdapter =new AnimateAdapter(entityData,gridLayoutManager,DEFAULT_SPAN_COUNT);
        mRecyclerView.setAdapter(mAnimateAdapter);
        return view;
    }

    public void setDynamicPreView()
    {
        Long startTime=System.currentTimeMillis();
        mAnimateAdapter.addItem(new CategoryEntity("当季新番"));
        initDynamicPreViewData();
        mAnimateAdapter.addItem(new CategoryEntity("上季新番"));
        initDynamicPreViewData();
        mAnimateAdapter.addItem(new CategoryEntity("经典推荐"));
        initDynamicPreViewData();
        Long endTime=System.currentTimeMillis();
        Log.d("setDynamic Time:", String.valueOf(endTime-startTime));
    }

    public void setDownloadData()
    {
        if(isNetworkConnected(getActivity().getApplicationContext()))
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

    public void setRefreshData()
    {
        new Thread(new refreshVideoInfo()).start();
    }

    public class downloadVideoInfo implements Runnable{
        @Override
        public void run() {
            jsonString= HttpUtils.getJsonContent(path);
            Log.d("TAG",jsonString);
            try {
                List<Integer> randList;
                JSONArray jsonArray=new JSONArray(jsonString);
                Log.d("jsonArray长度", String.valueOf(jsonArray.length()));
                randList=getRandom(jsonArray);
                if(randList.size()==12)
                {
                    setEntitiesDataFromJson(mDownloadVideoEntities,jsonArray,randList);
                    sendMessage(mHandler,IS_FINISH);
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

    public class refreshVideoInfo implements Runnable{
        @Override
        public void run() {
            jsonString= HttpUtils.getJsonContent(path);
            Log.d("TAG",jsonString);
            try {
                if(mPreVideoEntities.size()==0)
                {
                    setDownloadData();
                }
                else
                {
                    List<Integer> randList;
                    JSONArray jsonArray=new JSONArray(jsonString);
                    Log.d("jsonArray长度", String.valueOf(jsonArray.length()));
                    randList=getRandom(jsonArray);
                    if(randList.size()==12)
                    {
                        mRefreshVideoEntities.clear();
                        setEntitiesDataFromJson(mRefreshVideoEntities,jsonArray,randList);
                        sendMessage(mHandler,REFRESH_COMPLETE);
                    }
                }
            } catch (JSONException e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(mHandler,NO_DATA_GET);
                    }
                }).start();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRefresh() {
        setRefreshData();
    }

    public List<Integer> getRandom(JSONArray jsonArray)
    {
        Random rand=new Random();
        List<Integer> randList=new ArrayList<>();
        for (int j=0;j<jsonArray.length();)
        {
            int randInt=rand.nextInt(jsonArray.length());
            if(randList.size()==0)
            {
                randList.add(randInt);
                j++;
                Log.d("随机值", String.valueOf(randInt));
                if(randList.size()==12)
                    break;
            }
            else
            {
                if(randList.contains(randInt))
                {
                    continue;
                }
                else {
                    randList.add(randInt);
                    j++;
                    if(randList.size()==12)
                        break;
                }
                Log.d("随机值", String.valueOf(randInt));
            }
        }
        return randList;
    }

    public void setEntitiesData(List<VideoEntity> mPreEntities,List<VideoEntity> mEntities)
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

    public void initDynamicPreViewData()
    {
        for (int i=0;i<4;i++)
        {
            mPreVideoEntity=new VideoEntity();
            mAnimateAdapter.addItem(mPreVideoEntity);
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
}
