package com.example.tonyw.acgwarehouse.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.activity.SeriesDetailActivity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private int mDefaultSpanCount;
    private Activity mResourceActivity;
    public ResourceAdapter(List<Entity> entityList,int defaultSpanCount,Activity context)
    {
        mEntityList=entityList;
        mDefaultSpanCount=defaultSpanCount;
        mResourceActivity=context;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_resource_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder, int position) {
        ImageView resourceThumb= (ImageView) holder.itemView.findViewById(R.id.resourceThumb);
        TextView resourceTitle= (TextView) holder.itemView.findViewById(R.id.resourceTitle);
        resourceThumb.setImageBitmap(mEntityList.get(position).getVideoThumbBitmap());
        resourceTitle.setText(mEntityList.get(position).getVideoTitle());
        holder.setIsRecyclable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mResourceActivity, SeriesDetailActivity.class);
                it.putExtra("videoTitle",mEntityList.get(holder.getAdapterPosition()).getVideoTitle());
                it.putExtra("videoIntro",mEntityList.get(holder.getAdapterPosition()).getVideoIntro());
                it.putExtra("videoUrl",mEntityList.get(holder.getAdapterPosition()).getVideoUrl());
                it.putExtra("allVideoEpisode",mEntityList.get(holder.getAdapterPosition()).getAllVideoEpisode());
                mResourceActivity.startActivity(it);
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
