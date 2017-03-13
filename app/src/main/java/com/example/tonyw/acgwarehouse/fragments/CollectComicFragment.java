package com.example.tonyw.acgwarehouse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.CollectComicAdapter;
import com.example.tonyw.acgwarehouse.entity.CollectAnimateEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonywu10 on 2016/12/12.
 */

public class CollectComicFragment extends Fragment{
    public static final int DEFAULT_SPAN_COUNT=2;
    private RecyclerView mRecyclerView;
    private List<Entity> entityData=new ArrayList<>();
    private CollectComicAdapter mCollectComicAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_collect_comic, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.collect_comic_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mCollectComicAdapter =new CollectComicAdapter(entityData);
        mRecyclerView.setAdapter(mCollectComicAdapter);
        getData();
        return view;
    }

    public void getData() {
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"first",123,true));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"second",23,false));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"third",3,true));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"fourth",3123,false));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"first",123,true));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"second",23,false));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"third",3,true));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"fourth",3123,false));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"first",123,true));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"second",23,false));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"third",3,true));
        mCollectComicAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"fourth",3123,false));
    }
}
