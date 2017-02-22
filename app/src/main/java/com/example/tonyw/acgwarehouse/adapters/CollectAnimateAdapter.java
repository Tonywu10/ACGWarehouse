package com.example.tonyw.acgwarehouse.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.utils.Entity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;

import java.util.List;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class CollectAnimateAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    private int mDefaultSpanCount;
    public CollectAnimateAdapter(List<Entity> entityList, GridLayoutManager gridLayoutManager,int defaultSpanCount)
    {
        mEntityList=entityList;
        mDefaultSpanCount=defaultSpanCount;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_collect_animate_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        ImageView collectVideoThumb= (ImageView) holder.itemView.findViewById(R.id.collectVideoThumb);
        TextView collectVideoTitle= (TextView) holder.itemView.findViewById(R.id.collectVideoTitle);
        TextView collectNewEpisode= (TextView) holder.itemView.findViewById(R.id.collectNewEpisode);
        TextView isVideoUpdate= (TextView) holder.itemView.findViewById(R.id.isVideoUpdate);
        collectVideoThumb.setImageResource(mEntityList.get(position).getCollectThumb());
        collectVideoTitle.setText(mEntityList.get(position).getCollectTitle());
        collectNewEpisode.setText(String.valueOf(mEntityList.get(position).getCollectNewEpisode()));
        if(mEntityList.get(position).collectIsUpdate()==true)
        {
            isVideoUpdate.setText("有更新");
        }
        else
        {
            isVideoUpdate.setText("没更新");
        }
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

