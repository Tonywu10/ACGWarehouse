package com.example.tonyw.acgwarehouse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.DownloadingAdapter;
import com.example.tonyw.acgwarehouse.entity.DownloadingEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonywu10 on 2016/12/10.
 */

public class DownloadingFragment extends Fragment{
    private RecyclerView mRecyclerView;
    private List<Entity> entityData=new ArrayList<>();
    private DownloadingAdapter mDownloadingAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_downloading, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.downloading_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mDownloadingAdapter =new DownloadingAdapter(entityData);
        mRecyclerView.setAdapter(mDownloadingAdapter);
        getData();
        return view;
    }

    public void getData() {
        mDownloadingAdapter.addItem(new DownloadingEntity(R.drawable.whale,"文件标题",20,100));
        mDownloadingAdapter.addItem(new DownloadingEntity(R.drawable.comic,"文件标题",32,200));
        mDownloadingAdapter.addItem(new DownloadingEntity(R.drawable.whale,"文件标题",36,300));
        mDownloadingAdapter.addItem(new DownloadingEntity(R.drawable.comic,"文件标题",4,400));
        mDownloadingAdapter.addItem(new DownloadingEntity(R.drawable.whale,"文件标题",98,500));
        mDownloadingAdapter.addItem(new DownloadingEntity(R.drawable.comic,"文件标题",67,600));
    }
}
