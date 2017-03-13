package com.example.tonyw.acgwarehouse.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.activity.PlayActivity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

public class SeriesDetailAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private int mDefaultSpanCount;
    private Activity mSeriesDetailActivity;
    public SeriesDetailAdapter(List<Entity> entityList,int defaultSpanCount,Activity context)
    {
        mEntityList=entityList;
        mDefaultSpanCount=defaultSpanCount;
        mSeriesDetailActivity=context;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_series_detail_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder,int position) {
        Button seriesDetailEpisode= (Button) holder.itemView.findViewById(R.id.seriesDetailEpisode);
        seriesDetailEpisode.setText(String.valueOf(mEntityList.get(position).getSeriesEpisode()));
        seriesDetailEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mSeriesDetailActivity, PlayActivity.class);
                it.putExtra("videoId",mEntityList.get(holder.getAdapterPosition()).getPlayUrl());
                mSeriesDetailActivity.startActivity(it);
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
