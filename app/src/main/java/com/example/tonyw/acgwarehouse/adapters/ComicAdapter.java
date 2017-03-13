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

public class ComicAdapter extends RecyclerView.Adapter<BaseHolder>{
    private int mDefaultSpanCount;
    private List<Entity> mEntityList;
    public ComicAdapter(List<Entity> entityList, GridLayoutManager gridLayoutManager, int defaultSpanCount)
    {
        mEntityList=entityList;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isCategoryType(position)?mDefaultSpanCount:1;
            }
        });
        mDefaultSpanCount=defaultSpanCount;
    }

    private boolean isCategoryType(int position) {
        return mEntityList.get(position).getItemType()== Entity.CATEGORY_ITEM_TYPE;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mEntityList.get(position).getItemType()==Entity.CATEGORY_ITEM_TYPE?0:2;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==0)
        {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_comic_category,parent,false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_comic_item, parent, false);
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
            bindComicItem(holder,position);
        }
    }

    private void bindCategoryItem(BaseHolder holder, int position) {
        TextView title= (TextView) holder.itemView.findViewById(R.id.category_name);
        title.setText(mEntityList.get(position).getCategoryName());
    }

    private void bindComicItem(BaseHolder holder, int position) {
        ImageView comicThumb= (ImageView) holder.itemView.findViewById(R.id.comicThumb);
        TextView comicTitle= (TextView) holder.itemView.findViewById(R.id.comicTitle);
        TextView newEpisode= (TextView) holder.itemView.findViewById(R.id.newEpisode);
        comicThumb.setImageResource(mEntityList.get(position).getComicThumb());
        comicTitle.setText(mEntityList.get(position).getComicTitle());
        newEpisode.setText(String.valueOf(mEntityList.get(position).getNewEpisode()));
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
