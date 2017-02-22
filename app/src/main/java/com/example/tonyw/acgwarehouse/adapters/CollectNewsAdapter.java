package com.example.tonyw.acgwarehouse.adapters;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.activity.CollectActivity;
import com.example.tonyw.acgwarehouse.activity.NewsDetailActivity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class CollectNewsAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private View view;
    public CollectNewsAdapter(List<Entity> entityList, LinearLayoutManager linearLayoutManager)
    {
        mEntityList=entityList;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_collect_news_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, final int position) {
        ImageView collectNewsThumb= (ImageView) holder.itemView.findViewById(R.id.collectNewsThumb);
        TextView collectNewsTitle= (TextView) holder.itemView.findViewById(R.id.collectNewsTitle);
        TextView collectNewsSource= (TextView) holder.itemView.findViewById(R.id.collectNewsSource);
        TextView collectNewsDate= (TextView) holder.itemView.findViewById(R.id.collectNewsDate);
        collectNewsThumb.setImageBitmap(mEntityList.get(position).getNewsThumbBitmap());
        collectNewsTitle.setText(mEntityList.get(position).getNewsTitle());
        collectNewsSource.setText(mEntityList.get(position).getNewsSource());
        collectNewsDate.setText(mEntityList.get(position).getNewsDate());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(CollectActivity.collectActivity, NewsDetailActivity.class);
                it.putExtra("news_url",mEntityList.get(position).getNewsUrl());
                it.putExtra("NewsTitle",mEntityList.get(position).getNewsTitle());
                CollectActivity.collectActivity.startActivity(it);
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
