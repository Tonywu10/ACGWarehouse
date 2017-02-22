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
import com.example.tonyw.acgwarehouse.activity.MainActivity;
import com.example.tonyw.acgwarehouse.activity.NewsDetailActivity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

/**
 * Created by tonywu10 on 2016/11/29.
 */

public class NewsAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private View view;
    public NewsAdapter(List<Entity> entityList, LinearLayoutManager linearLayoutManager)
    {
        mEntityList=entityList;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_news_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, final int position) {

        ImageView newsThumb= (ImageView) holder.itemView.findViewById(R.id.newsThumb);
        TextView newsTitle= (TextView) holder.itemView.findViewById(R.id.newsTitle);
        TextView newSource= (TextView) holder.itemView.findViewById(R.id.newsSource);
        TextView newsDate= (TextView) holder.itemView.findViewById(R.id.newsDate);
        newsThumb.setImageBitmap(mEntityList.get(position).getNewsThumbBitmap());
        newsTitle.setText(mEntityList.get(position).getNewsTitle());
        newSource.setText(mEntityList.get(position).getNewsSource());
        newsDate.setText(mEntityList.get(position).getNewsDate());
        holder.setIsRecyclable(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(MainActivity.mainActivity, NewsDetailActivity.class);
                it.putExtra("news_url",mEntityList.get(position).getNewsUrl());
                it.putExtra("NewsTitle",mEntityList.get(position).getNewsTitle());
                MainActivity.mainActivity.startActivity(it);
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
    public void addItem(int location,Entity item)
    {
        mEntityList.add(location,item);
        notifyDataSetChanged();
    }
    public void removeItem(Entity item)
    {
        mEntityList.remove(item);
        notifyDataSetChanged();
    }
}