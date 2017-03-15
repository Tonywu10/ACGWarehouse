package com.example.tonyw.acgwarehouse.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.DEFAULT_SPAN_COUNT;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_FINISH;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_DATA_GET;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_NETWORK;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.REFRESH_COMPLETE;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.isNetworkConnected;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

public class AnimateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private AnimateAdapter mAnimateAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Entity> entityData=new ArrayList<>();
    private List<VideoEntity> mPreVideoEntities=new ArrayList<>();
    private List<VideoEntity> mDownloadVideoEntities=new ArrayList<>();
    private List<VideoEntity> mRefreshVideoEntities=new ArrayList<>();
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
                    setEntitiesData(mPreVideoEntities,mDownloadVideoEntities);
                    mAnimateAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_NETWORK:
                    Toast.makeText(getActivity().getApplicationContext(),"网络出错",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_DATA_GET:
                    Toast.makeText(getActivity().getApplicationContext(),"无法获取数据，请刷新",Toast.LENGTH_SHORT).show();
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
        RecyclerView mRecyclerView= (RecyclerView) view.findViewById(R.id.animate_recyclerview);
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
        mAnimateAdapter =new AnimateAdapter(entityData,gridLayoutManager,DEFAULT_SPAN_COUNT,getActivity());
        mRecyclerView.setAdapter(mAnimateAdapter);
        return view;
    }

    public void setDynamicPreView()
    {
        mAnimateAdapter.addItem(new CategoryEntity("当季新番"));
        initDynamicPreViewData();
        mAnimateAdapter.addItem(new CategoryEntity("上季新番"));
        initDynamicPreViewData();
        mAnimateAdapter.addItem(new CategoryEntity("经典推荐"));
        initDynamicPreViewData();
    }

    public void setDownloadData()
    {
        if(isNetworkConnected(getActivity().getApplicationContext()))
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

    public void setRefreshData()
    {
        new Thread(new refreshVideoInfo()).start();
    }

    private class downloadVideoInfo implements Runnable{
        @Override
        public void run() {
            jsonString= HttpUtils.getJsonContent(path);
            try {
                List<Integer> randList;
                JSONArray jsonArray=new JSONArray(jsonString);
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

    private class refreshVideoInfo implements Runnable{
        @Override
        public void run() {
            jsonString= HttpUtils.getJsonContent(path);
            try {
                if(mPreVideoEntities.size()==0)
                {
                    setDownloadData();
                }
                else
                {
                    List<Integer> randList;
                    JSONArray jsonArray=new JSONArray(jsonString);
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

    //根据返回Json数组的长度获取随机值
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
                if(randList.size()==12)
                    break;
            }
            else
            {
                if(randList.contains(randInt))
                {

                }
                else {
                    randList.add(randInt);
                    j++;
                    if(randList.size()==12)
                        break;
                }
            }
        }
        return randList;
    }

    public void setEntitiesData(List<VideoEntity> mPreEntities,List<VideoEntity> mEntities)
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

    public void initDynamicPreViewData()
    {
        VideoEntity mPreVideoEntity;
        for (int i=0;i<4;i++)
        {
            mPreVideoEntity=new VideoEntity();
            mAnimateAdapter.addItem(mPreVideoEntity);
            mPreVideoEntities.add(mPreVideoEntity);
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
}
