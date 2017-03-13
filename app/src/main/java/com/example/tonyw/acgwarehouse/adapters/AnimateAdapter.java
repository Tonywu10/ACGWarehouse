package com.example.tonyw.acgwarehouse.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.activity.ResourceActivity;
import com.example.tonyw.acgwarehouse.activity.SeriesDetailActivity;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

public class AnimateAdapter extends RecyclerView.Adapter<BaseHolder>{
    private int mDefaultSpanCount;
    private List<Entity> mEntityList;
    private View view;
    private Activity mMainActivity;
    public AnimateAdapter(List<Entity> entityList,GridLayoutManager gridLayoutManager,int defaultSpanCount,Activity context)
    {
        mEntityList=entityList;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isCategoryType(position)?mDefaultSpanCount:1;
            }
        });
        mDefaultSpanCount=defaultSpanCount;
        mMainActivity=context;
    }

    private boolean isCategoryType(int position) {
        return mEntityList.get(position).getItemType()== Entity.CATEGORY_ITEM_TYPE;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mEntityList.get(position).getItemType()==Entity.CATEGORY_ITEM_TYPE?0:1;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_animate_category,parent,false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_animate_item, parent, false);
        }
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if(isCategoryType(position))
        {
            bindCategoryItem(holder,position);
        }
        else
        {
            bindVideoItem(holder,position);
        }
    }

    private void bindCategoryItem(BaseHolder holder, int position) {
        TextView title= (TextView) holder.itemView.findViewById(R.id.category_name);
        title.setText(mEntityList.get(position).getCategoryName());
        Button more= (Button) holder.itemView.findViewById(R.id.category_click_more);
        holder.setIsRecyclable(false);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(mMainActivity, ResourceActivity.class);
                mMainActivity.startActivity(it);
            }
        });
    }

    private void bindVideoItem(BaseHolder holder, final int position) {
        ImageView videoThumb= (ImageView) holder.itemView.findViewById(R.id.videoThumb);
        TextView videoTitle= (TextView) holder.itemView.findViewById(R.id.videoTitle);
        videoThumb.setImageBitmap(mEntityList.get(position).getVideoThumbBitmap());
        videoTitle.setText(mEntityList.get(position).getVideoTitle());
        holder.setIsRecyclable(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("videoTitle",mEntityList.get(position).getVideoTitle());
                Log.d("videoIntro",mEntityList.get(position).getVideoIntro());
                Intent it=new Intent(mMainActivity, SeriesDetailActivity.class);
                it.putExtra("videoTitle",mEntityList.get(position).getVideoTitle());
                it.putExtra("videoIntro",mEntityList.get(position).getVideoIntro());
                it.putExtra("videoUrl",mEntityList.get(position).getVideoUrl());
                it.putExtra("allVideoEpisode",mEntityList.get(position).getAllVideoEpisode());
                mMainActivity.startActivity(it);
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
