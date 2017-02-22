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
import com.example.tonyw.acgwarehouse.adapters.ComicAdapter;
import com.example.tonyw.acgwarehouse.entity.CategoryEntity;
import com.example.tonyw.acgwarehouse.entity.ComicEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonywu10 on 2016/11/28.
 */

public class ComicFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public static final int DEFAULT_SPAN_COUNT=4;
    private RecyclerView mRecyclerView;
    private List<Entity> entityData=new ArrayList<>();
    private ComicAdapter mComicAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int REFRESH_COMPLETE=100;
    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case REFRESH_COMPLETE:
                    Toast.makeText(getActivity().getApplicationContext(),"Refresh Complete!",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_comic, container, false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.comic_recyclerview);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.comic_swipeRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(view.getContext(),DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mComicAdapter =new ComicAdapter(entityData,gridLayoutManager,DEFAULT_SPAN_COUNT);
        mRecyclerView.setAdapter(mComicAdapter);
        getData();
        return view;
    }

    public void getData() {
        mComicAdapter.addItem(new CategoryEntity("当季新番"));
        mComicAdapter.addItem(new ComicEntity(R.drawable.whale,"first",1));
        mComicAdapter.addItem(new ComicEntity(R.drawable.comic,"second",2));
        mComicAdapter.addItem(new ComicEntity(R.drawable.whale,"third",3));
        mComicAdapter.addItem(new ComicEntity(R.drawable.comic,"fourth",4));
        mComicAdapter.addItem(new CategoryEntity("上季新番"));
        mComicAdapter.addItem(new ComicEntity(R.drawable.whale,"first",1));
        mComicAdapter.addItem(new ComicEntity(R.drawable.comic,"second",2));
        mComicAdapter.addItem(new ComicEntity(R.drawable.whale,"third",3));
        mComicAdapter.addItem(new ComicEntity(R.drawable.comic,"fourth",4));
        mComicAdapter.addItem(new CategoryEntity("经典推荐"));
        mComicAdapter.addItem(new ComicEntity(R.drawable.whale,"first",1));
        mComicAdapter.addItem(new ComicEntity(R.drawable.comic,"second",2));
        mComicAdapter.addItem(new ComicEntity(R.drawable.whale,"third",3));
        mComicAdapter.addItem(new ComicEntity(R.drawable.comic,"fourth",4));
    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE,100);
    }
}