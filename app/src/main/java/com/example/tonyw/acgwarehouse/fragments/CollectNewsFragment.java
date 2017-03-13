package com.example.tonyw.acgwarehouse.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.CollectNewsAdapter;
import com.example.tonyw.acgwarehouse.entity.NewsEntity;
import com.example.tonyw.acgwarehouse.entity.UserEntity;
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
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.isNetworkConnected;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

public class CollectNewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private RecyclerView mRecyclerView;
    private List<Entity> entityData=new ArrayList<>();
    private CollectNewsAdapter mCollectNewsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String jsonString;
    private List<NewsEntity> mPreNewsCollectEntities=new ArrayList<>();
    private NewsEntity mPreNewsCollectEntity;
    private List<NewsEntity> mDownloadNewsCollectEntities=new ArrayList<>();
    private UserEntity mUserEntity;
    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case NO_NETWORK:
                    Log.d("no_network","I'm in");
                    Toast.makeText(getActivity().getApplicationContext(),"network is down",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_DATA_GET:
                    Log.d("no_network","I'm in");
                    Toast.makeText(getActivity().getApplicationContext(),"network is down",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case IS_FINISH:
                    setDynamicPreView();
                    Long startTime=System.currentTimeMillis();
                    setEntitiesData(mPreNewsCollectEntities,mDownloadNewsCollectEntities);
                    Long endTime=System.currentTimeMillis();
                    Log.d("finish time", String.valueOf(endTime-startTime));
                    mCollectNewsAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_collect_news, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.collect_news_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.news_collect_swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mCollectNewsAdapter =new CollectNewsAdapter(entityData,getActivity());
        mRecyclerView.setAdapter(mCollectNewsAdapter);
        mUserEntity= (UserEntity) getActivity().getApplication();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&(mPreNewsCollectEntities.size()==0)){
            setDownloadData();
        }
    }

    public void setDynamicPreView()
    {
        initDynamicPreViewData();
    }

    public void setDownloadData()
    {
        if(isNetworkConnected(getActivity().getApplicationContext()))
        {
            Log.d("start downloading","hello");
            new Thread(new downloadNewsCollectInfo()).start();
        }
        else
        {
            Log.d("TAG","thread in");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("Thread","Im in");
                        Thread.sleep(2000);
                        sendMessage(mHandler,NO_NETWORK);
                    }catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private class downloadNewsCollectInfo implements Runnable
    {
        @Override
        public void run() {
            try {
                String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsCollectDemo?userName="+mUserEntity.getUserName();
                jsonString= HttpUtils.getJsonContent(path);
                List<Integer> randList;
                JSONArray jsonArray = new JSONArray(jsonString);
                randList=getSequence(jsonArray);
                setEntitiesDataFromJson(mDownloadNewsCollectEntities,jsonArray,randList);
                sendMessage(mHandler,IS_FINISH);
            } catch (JSONException e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("Haaaaa","Im in");
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

    @Override
    public void onRefresh() {
        for(int i = 0 ;i< mPreNewsCollectEntities.size();i++){
            mCollectNewsAdapter.removeItem(mPreNewsCollectEntities.get(i));
        }
        for(int i = 0 , len= mPreNewsCollectEntities.size();i<len;++i){
            mPreNewsCollectEntities.remove(i);
            --len;
            --i;
        }
        for(int i = 0 , len= mDownloadNewsCollectEntities.size();i<len;++i){
            mDownloadNewsCollectEntities.remove(i);
            --len;
            --i;
        }
        setDownloadData();
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

    public void setEntitiesData(List<NewsEntity> mPreEntities,List<NewsEntity> mEntities)
    {
        for (int i = 0; i < mPreEntities.size(); i++) {
            Log.d("mEntities长度", String.valueOf(mEntities.size()));
            mPreEntities.get(i).setNewsThumbBitmap(mEntities.get(i).getNewsThumbBitmap());
            mPreEntities.get(i).setNewsTitle(mEntities.get(i).getNewsTitle());
            mPreEntities.get(i).setNewsSource("动漫星空");
            mPreEntities.get(i).setNewsDate(mEntities.get(i).getNewsDate());
            mPreEntities.get(i).setNewsUrl(mEntities.get(i).getNewsUrl());
        }
    }

    public void initDynamicPreViewData()
    {
        for (int i=0;i<mDownloadNewsCollectEntities.size();i++)
        {
            mPreNewsCollectEntity=new NewsEntity();
            mCollectNewsAdapter.addItem(mPreNewsCollectEntity);
            mPreNewsCollectEntities.add(mPreNewsCollectEntity);
        }
    }

    public void setEntitiesDataFromJson(List<NewsEntity> mEntities,JSONArray jsonArray,List<Integer> randList) throws JSONException {
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(randList.get(i));
            mPreNewsCollectEntity=new NewsEntity();
            mPreNewsCollectEntity.setNewsThumbBitmap(getHttpBitmap(jsonObject.getString("NewsThumb")));
            mPreNewsCollectEntity.setNewsTitle(jsonObject.getString("NewsTitle"));
            mPreNewsCollectEntity.setNewsSource("动漫星空");
            mPreNewsCollectEntity.setNewsDate(jsonObject.getString("UpdateTime"));
            mPreNewsCollectEntity.setNewsUrl(jsonObject.getString("NewsUrl"));
            mEntities.add(mPreNewsCollectEntity);
        }
    }
}
