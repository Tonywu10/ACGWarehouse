package com.example.tonyw.acgwarehouse.adapters;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.activity.PlayActivity;
import com.example.tonyw.acgwarehouse.activity.SeriesDetailActivity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

/**
 * Created by tonyw on 2016/12/23.
 */

public class SeriesDetailAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private int mDefaultSpanCount;
    public SeriesDetailAdapter(List<Entity> entityList, GridLayoutManager gridLayoutManager, int defaultSpanCount)
    {
        mEntityList=entityList;
        mDefaultSpanCount=defaultSpanCount;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_series_detail_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, final int position) {
        Button seriesDetailEpisode= (Button) holder.itemView.findViewById(R.id.seriesDetailEpisode);
        seriesDetailEpisode.setText(String.valueOf(mEntityList.get(position).getSeriesEpisode()));
        seriesDetailEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(SeriesDetailActivity.seriesDetailActivity, PlayActivity.class);
                it.putExtra("videoId",mEntityList.get(position).getPlayUrl());
                SeriesDetailActivity.seriesDetailActivity.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEntityList.size();
    }

    public void addItem(Entity item)
    {
        mEntityList.add(item);
        notifyDataSetChanged();
    }
    public void removeItem(Entity item)
    {
        mEntityList.remove(item);
        notifyDataSetChanged();
    }
}
