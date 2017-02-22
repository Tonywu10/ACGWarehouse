package com.example.tonyw.acgwarehouse.adapters;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.activity.ResourceActivity;
import com.example.tonyw.acgwarehouse.activity.SeriesDetailActivity;
import com.example.tonyw.acgwarehouse.utils.Entity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;

import java.util.List;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class ResourceAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private int mDefaultSpanCount;
    public ResourceAdapter(List<Entity> entityList, GridLayoutManager gridLayoutManager,int defaultSpanCount)
    {
        mEntityList=entityList;
        mDefaultSpanCount=defaultSpanCount;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_resource_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, final int position) {
        ImageView resourceThumb= (ImageView) holder.itemView.findViewById(R.id.resourceThumb);
        TextView resourceTitle= (TextView) holder.itemView.findViewById(R.id.resourceTitle);
        resourceThumb.setImageBitmap(mEntityList.get(position).getVideoThumbBitmap());
        resourceTitle.setText(mEntityList.get(position).getVideoTitle());
        holder.setIsRecyclable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(ResourceActivity.resourceActivity, SeriesDetailActivity.class);
                it.putExtra("videoTitle",mEntityList.get(position).getVideoTitle());
                it.putExtra("videoIntro",mEntityList.get(position).getVideoIntro());
                it.putExtra("videoUrl",mEntityList.get(position).getVideoUrl());
                it.putExtra("allVideoEpisode",mEntityList.get(position).getAllVideoEpisode());
                ResourceActivity.resourceActivity.startActivity(it);
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
