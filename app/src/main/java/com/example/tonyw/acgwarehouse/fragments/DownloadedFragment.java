package com.example.tonyw.acgwarehouse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.DownloadedAdapter;
import com.example.tonyw.acgwarehouse.entity.DownloadedEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonywu10 on 2016/12/10.
 */

public class DownloadedFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private List<Entity> entityData=new ArrayList<>();
    private DownloadedAdapter mDownloadedAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_downloaded, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.downloaded_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mDownloadedAdapter =new DownloadedAdapter(entityData,linearLayoutManager);
        mRecyclerView.setAdapter(mDownloadedAdapter);
        getData();
        return view;
    }

    public void getData() {
        mDownloadedAdapter.addItem(new DownloadedEntity(R.drawable.whale,"文件标题",1000,"视频"));
        mDownloadedAdapter.addItem(new DownloadedEntity(R.drawable.comic,"文件标题",2000,"漫画"));
        mDownloadedAdapter.addItem(new DownloadedEntity(R.drawable.whale,"文件标题",3000,"视频"));
        mDownloadedAdapter.addItem(new DownloadedEntity(R.drawable.comic,"文件标题",4000,"漫画"));
        mDownloadedAdapter.addItem(new DownloadedEntity(R.drawable.whale,"文件标题",5000,"视频"));
        mDownloadedAdapter.addItem(new DownloadedEntity(R.drawable.comic,"文件标题",6000,"漫画"));
    }
}
