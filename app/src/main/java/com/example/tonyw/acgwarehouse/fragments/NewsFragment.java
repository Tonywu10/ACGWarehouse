package com.example.tonyw.acgwarehouse.fragments;

import android.app.ProgressDialog;
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

import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_FINISH;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.LOAD_MORE_DATA;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_DATA_GET;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.NO_NETWORK;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.REFRESH_COMPLETE;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.isNetworkConnected;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private boolean isLoadingMore = true;
    private NewsAdapter mNewsAdapter;
    private ProgressDialog mProgressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Entity> entityData=new ArrayList<>();
    private List<NewsEntity> mPreNewsEntities=new ArrayList<>();
    private List<NewsEntity> mDownloadNewsEntities=new ArrayList<>();
    private List<NewsEntity> mRefreshNewsEntities=new ArrayList<>();

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
                    setEntitiesData(mPreNewsEntities,mDownloadNewsEntities);
                    mNewsAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_NETWORK:
                    Log.d("no_network","I'm in");
                    Toast.makeText(getActivity().getApplicationContext(),"网络出错",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case NO_DATA_GET:
                    Log.d("no_data_get","I'm in");
                    Toast.makeText(getActivity().getApplicationContext(),"无法获取数据，请刷新",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case LOAD_MORE_DATA:
                    mProgressDialog.dismiss();
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
        RecyclerView mRecyclerView;
        mRecyclerView= (RecyclerView) view.findViewById(R.id.news_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mProgressDialog=new ProgressDialog(getActivity());
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
        mNewsAdapter =new NewsAdapter(entityData,getActivity());
        mRecyclerView.setAdapter(mNewsAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = linearLayoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount-1 && dy > 0) {
                    if(isLoadingMore){
                        isLoadingMore = false;
                        mProgressDialog.show();
                        mProgressDialog.setMessage("加载中");
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        new Thread(new loadNewsInfo()).start();
                    }
                }
            }
        });
        return view;
    }

    public void setDynamicPreView()
    {
        NewsEntity mPreNewsEntity;
        for (int i=0;i<mDownloadNewsEntities.size();i++)
        {
            mPreNewsEntity=new NewsEntity();
            mNewsAdapter.addItem(mPreNewsEntity);
            mPreNewsEntities.add(mPreNewsEntity);
        }
    }

    public void setDownloadData()
    {
        if(isNetworkConnected(getActivity().getApplicationContext()))
        {
            new Thread(new downloadNewsInfo()).start();
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
        new Thread(new refreshNewsInfo()).start();
    }

    private class loadNewsInfo implements Runnable
    {
        @Override
        public void run() {
            try {
                List<Integer> randList;
                String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsLoadDemo?lastUrl="+mPreNewsEntities.get(mPreNewsEntities.size()-1).getNewsUrl();
                JSONArray jsonArray = new JSONArray(getJsonData(path));
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

    private class downloadNewsInfo implements Runnable{
        @Override
        public void run() {
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsDemo";
            String jsonString = HttpUtils.getJsonContent(path);
            try {
                List<Integer> randList;
                JSONArray jsonArray=new JSONArray(jsonString);
                randList=getSequence(jsonArray);
                setEntitiesDataFromJson(mDownloadNewsEntities,jsonArray,randList);
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

    private class refreshNewsInfo implements Runnable{
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
                    List<Integer> randList;
                    String path="http://tonywu10.imwork.net:16284/ACGWarehouse/NewsRefreshDemo?first_url="+firstUrl;
                    JSONArray jsonArray=new JSONArray(getJsonData(path));
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

    public void setEntitiesData(List<NewsEntity> mPreEntities,List<NewsEntity> mEntities)
    {
        for (int i = 0; i < mPreEntities.size(); i++) {
            mPreEntities.get(i).setNewsThumbBitmap(mEntities.get(i).getNewsThumbBitmap());
            mPreEntities.get(i).setNewsTitle(mEntities.get(i).getNewsTitle());
            mPreEntities.get(i).setNewsSource("动漫星空");
            mPreEntities.get(i).setNewsDate(mEntities.get(i).getNewsDate());
            mPreEntities.get(i).setNewsUrl(mEntities.get(i).getNewsUrl());
        }

    }

    public void setEntitiesDataFromJson(List<NewsEntity> mEntities,JSONArray jsonArray,List<Integer> randList) throws JSONException {
        NewsEntity mRefreshNewsEntity;
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