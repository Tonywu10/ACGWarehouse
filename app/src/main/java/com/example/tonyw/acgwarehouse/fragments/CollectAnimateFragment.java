package com.example.tonyw.acgwarehouse.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.CollectAnimateAdapter;
import com.example.tonyw.acgwarehouse.entity.CollectAnimateEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.ArrayList;
import java.util.List;

public class CollectAnimateFragment extends Fragment{
    public static final int DEFAULT_SPAN_COUNT=2;
    private List<Entity> entityData=new ArrayList<>();
    private CollectAnimateAdapter mCollectAnimateAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_collect_animate, container, false);
        RecyclerView mRecyclerView;
        mRecyclerView= (RecyclerView) view.findViewById(R.id.collect_animate_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mCollectAnimateAdapter =new CollectAnimateAdapter(entityData);
        mRecyclerView.setAdapter(mCollectAnimateAdapter);
        getData();
        return view;
    }

    public void getData() {
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"first",123,true));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"second",23,false));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"third",3,true));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"fourth",3123,false));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"first",123,true));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"second",23,false));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"third",3,true));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"fourth",3123,false));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"first",123,true));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"second",23,false));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.whale,"third",3,true));
        mCollectAnimateAdapter.addItem(new CollectAnimateEntity(R.drawable.comic,"fourth",3123,false));
    }
}
