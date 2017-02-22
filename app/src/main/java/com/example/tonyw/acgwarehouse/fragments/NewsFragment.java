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
import com.example.tonyw.acgwarehouse.adapters.NewsAdapter;
import com.example.tonyw.acgwarehouse.entity.NewsEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;
import com.example.tonyw.acgwarehouse.utils.HttpUtils;

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
 * Created by tonywu10 on 2016/11/28.
 */

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private static final int REFRESH_COMPLETE=100;
    private static final int IS_FINISH=101;
    public static final int LOAD_MORE_DATA=102;
    private static final int NO_NETWORK=103;
    private static final int NO_DATA_GET=104;

    private boolean isLoadingMore = true;

    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Entity> entityData=new ArrayList<>();
    private List<NewsEntity> mPreNewsEntities=new ArrayList<>();
    private NewsEntity mPreNewsEntity;
    private List<NewsEntity> mDownloadNewsEntities=new ArrayList<>();
    private List<NewsEntity> mRefreshNewsEntities=new ArrayList<>();
    private NewsEntity mRefreshNewsEntity;

    private String jsonString;
    private String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsDemo";

    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    for (int i=0;i<mRefreshNewsEntities.size();i++)
                    {
                        Log.d("entity",mRefreshNewsEntities.get(i).getNewsTitle());
                        mNewsAdapter.addItem(0,mRefreshNewsEntities.get(i));
                        mPreNewsEntities.add(0,mRefreshNewsEntities.get(i));
                    }
                    mNewsAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case IS_FINISH:
                    setDynamicPreView();
                    Long startTime=System.currentTimeMillis();
                    setEntitiesData(mPreNewsEntities,mDownloadNewsEntities,true);
                    Long endTime=System.currentTimeMillis();
                    Log.d("finish time", String.valueOf(endTime-startTime));
                    mNewsAdapter.notifyDataSetChanged();
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
                case LOAD_MORE_DATA:
                    Log.d("mRefreshNewsEntitiesSize", String.valueOf(mRefreshNewsEntities.size()));
                    for (int i=0;i<mRefreshNewsEntities.size();i++)
                    {
                        Log.d("entity",mRefreshNewsEntities.get(i).getNewsTitle());
                        mNewsAdapter.addItem(mRefreshNewsEntities.get(i));
                        mPreNewsEntities.add(mRefreshNewsEntities.get(i));
                    }
                    isLoadingMore=true;
                    mNewsAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&(mPreNewsEntities.size()==0)){
            setDownloadData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.news_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.news_swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNewsAdapter =new NewsAdapter(entityData,linearLayoutManager);
        mRecyclerView.setAdapter(mNewsAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = linearLayoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if(isLoadingMore){
                        Log.d("开启Thread","success");
                        isLoadingMore = false;
                        new Thread(new loadNewsInfo()).start();
                    }
                }
            }
        });
        return view;
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
            new Thread(new downloadNewsInfo()).start();
        }
        else
        {
            Log.d("TAG","thread in");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("Thread","I'm in");
                        Thread.sleep(2000);
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
        new Thread(new refreshNewsInfo()).start();
    }

    public class loadNewsInfo implements Runnable
    {
        @Override
        public void run() {
            try {
                Log.d("loadNewsInfo","我进来了");
                List<Integer> randList;
                Log.d("lastUrl",mPreNewsEntities.get(mPreNewsEntities.size()-1).getNewsUrl());
                String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsLoadDemo?lastUrl="+mPreNewsEntities.get(mPreNewsEntities.size()-1).getNewsUrl();
                Log.d("mPreNewsEntitiesSize", String.valueOf(mPreNewsEntities.size()));
                JSONArray jsonArray;
                jsonArray = new JSONArray(getJsonData(path));
                Log.d("jsonArray数据",jsonArray.toString());
                Log.d("jsonArray长度", String.valueOf(jsonArray.length()));
                randList=getSequence(jsonArray);
                mRefreshNewsEntities.clear();
                setEntitiesDataFromJson(mRefreshNewsEntities,jsonArray,randList);
                isLoadingMore=false;
                sendMessage(mHandler,LOAD_MORE_DATA);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class downloadNewsInfo implements Runnable{
        @Override
        public void run() {
            jsonString= HttpUtils.getJsonContent(path);
            Log.d("TAG",jsonString);
            try {
                List<Integer> randList;
                JSONArray jsonArray=new JSONArray(jsonString);
                Log.d("jsonArray长度", String.valueOf(jsonArray.length()));
                randList=getSequence(jsonArray);
                setEntitiesDataFromJson(mDownloadNewsEntities,jsonArray,randList);
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

    public class refreshNewsInfo implements Runnable{
        @Override
        public void run() {
            try {
                if(mPreNewsEntities.size()==0)
                {
                    setDownloadData();
                }
                else
                {
                    String firstUrl=mPreNewsEntities.get(0).getNewsUrl();
                    Log.d("url",firstUrl);
                    List<Integer> randList;
                    String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsRefreshDemo?first_url="+firstUrl;
                    JSONArray jsonArray=new JSONArray(getJsonData(path));
                    Log.d("jsonArray数据",jsonArray.toString());
                    Log.d("jsonArray长度", String.valueOf(jsonArray.length()));
                    randList=getSequence(jsonArray);
                    mRefreshNewsEntities.clear();
                    setEntitiesDataFromJson(mRefreshNewsEntities,jsonArray,randList);
                    sendMessage(mHandler,REFRESH_COMPLETE);
                }
            } catch (JSONException e) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("Haha","Im in");
                            Thread.sleep(100);
                            sendMessage(mHandler,NO_DATA_GET);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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

    public List<Integer> getSequence(JSONArray jsonArray)
    {
        List<Integer> seqList=new ArrayList<>();
        for (int i=0;i<jsonArray.length();i++)
        {
            seqList.add(i);
        }
        return seqList;
    }

    public void setEntitiesData(List<NewsEntity> mPreEntities,List<NewsEntity> mEntities,boolean isDownloadedData)
    {
        if(isDownloadedData) {
            for (int i = 0; i < mPreEntities.size(); i++) {
                Log.d("mEntities长度", String.valueOf(mEntities.size()));
                mPreEntities.get(i).setNewsThumbBitmap(mEntities.get(i).getNewsThumbBitmap());
                mPreEntities.get(i).setNewsTitle(mEntities.get(i).getNewsTitle());
                mPreEntities.get(i).setNewsSource("动漫星空");
                mPreEntities.get(i).setNewsDate(mEntities.get(i).getNewsDate());
                mPreEntities.get(i).setNewsUrl(mEntities.get(i).getNewsUrl());
            }
        }
        else {
            for (int i = 0; i < mEntities.size(); i++) {
                Log.d("mEntities长度", String.valueOf(mEntities.size()));
                Log.d("mPreEntities长度", String.valueOf(mPreEntities.size()));
                mPreEntities.get(i).setNewsThumbBitmap(mEntities.get(i).getNewsThumbBitmap());
                mPreEntities.get(i).setNewsTitle(mEntities.get(i).getNewsTitle());
                mPreEntities.get(i).setNewsSource("动漫星空");
                mPreEntities.get(i).setNewsDate(mEntities.get(i).getNewsDate());
                mPreEntities.get(i).setNewsUrl(mEntities.get(i).getNewsUrl());
            }
        }
    }

    public void initDynamicPreViewData()
    {
        for (int i=0;i<mDownloadNewsEntities.size();i++)
        {
            mPreNewsEntity=new NewsEntity();
            mNewsAdapter.addItem(mPreNewsEntity);
            mPreNewsEntities.add(mPreNewsEntity);
        }
    }

    public void setEntitiesDataFromJson(List<NewsEntity> mEntities,JSONArray jsonArray,List<Integer> randList) throws JSONException {
        for (int i=0;i<jsonArray.length();i++)
        {
            JSONObject jsonObject = jsonArray.getJSONObject(randList.get(i));
            mRefreshNewsEntity=new NewsEntity();
            mRefreshNewsEntity.setNewsThumbBitmap(getHttpBitmap(jsonObject.getString("NewsThumb")));
            mRefreshNewsEntity.setNewsTitle(jsonObject.getString("NewsTitle"));
            mRefreshNewsEntity.setNewsSource("动漫星空");
            mRefreshNewsEntity.setNewsDate(jsonObject.getString("UpdateTime"));
            mRefreshNewsEntity.setNewsUrl(jsonObject.getString("NewsUrl"));
            mEntities.add(mRefreshNewsEntity);
        }
    }
}