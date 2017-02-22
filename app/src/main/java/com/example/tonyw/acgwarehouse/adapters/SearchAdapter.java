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
import com.example.tonyw.acgwarehouse.activity.SearchActivity;
import com.example.tonyw.acgwarehouse.activity.SeriesDetailActivity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

import static com.example.tonyw.acgwarehouse.R.id.resourceThumb;
import static com.example.tonyw.acgwarehouse.R.id.resourceTitle;

/**
 * Created by tonyw on 2017/2/19.
 */

public class SearchAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private int mDefaultSpanCount;
    public SearchAdapter(List<Entity> entityList, GridLayoutManager gridLayoutManager, int defaultSpanCount)
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
        ImageView searchThumb= (ImageView) holder.itemView.findViewById(resourceThumb);
        TextView searchTitle= (TextView) holder.itemView.findViewById(resourceTitle);
        searchThumb.setImageBitmap(mEntityList.get(position).getVideoThumbBitmap());
        searchTitle.setText(mEntityList.get(position).getVideoTitle());
        holder.setIsRecyclable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(SearchActivity.searchActivity, SeriesDetailActivity.class);
                it.putExtra("videoTitle",mEntityList.get(position).getVideoTitle());
                it.putExtra("videoIntro",mEntityList.get(position).getVideoIntro());
                it.putExtra("videoUrl",mEntityList.get(position).getVideoUrl());
                it.putExtra("allVideoEpisode",mEntityList.get(position).getAllVideoEpisode());
                SearchActivity.searchActivity.startActivity(it);
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

