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

import static com.example.tonyw.acgwarehouse.R.id.resourceThumb;
import static com.example.tonyw.acgwarehouse.R.id.resourceTitle;

public class SearchAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private int mDefaultSpanCount;
    private Activity mSearchActivity;
    public SearchAdapter(List<Entity> entityList, int defaultSpanCount, Activity context)
    {
        mEntityList=entityList;
        mDefaultSpanCount=defaultSpanCount;
        mSearchActivity=context;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_resource_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseHolder holder,int position) {
        ImageView searchThumb= (ImageView) holder.itemView.findViewById(resourceThumb);
        TextView searchTitle= (TextView) holder.itemView.findViewById(resourceTitle);
        searchThumb.setImageBitmap(mEntityList.get(position).getVideoThumbBitmap());
        searchTitle.setText(mEntityList.get(position).getVideoTitle());
        holder.setIsRecyclable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mSearchActivity, SeriesDetailActivity.class);
                it.putExtra("videoTitle",mEntityList.get(holder.getAdapterPosition()).getVideoTitle());
                it.putExtra("videoIntro",mEntityList.get(holder.getAdapterPosition()).getVideoIntro());
                it.putExtra("videoUrl",mEntityList.get(holder.getAdapterPosition()).getVideoUrl());
                it.putExtra("allVideoEpisode",mEntityList.get(holder.getAdapterPosition()).getAllVideoEpisode());
                mSearchActivity.startActivity(it);
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

