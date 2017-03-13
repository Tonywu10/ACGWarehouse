package com.example.tonyw.acgwarehouse.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

/**
 * Created by tonywu10 on 2016/12/13.
 */

public class CollectComicAdapter extends RecyclerView.Adapter<BaseHolder>{
    private List<Entity> mEntityList;
    public CollectComicAdapter(List<Entity> entityList)
    {
        mEntityList=entityList;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_collect_comic_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        ImageView collectComicThumb= (ImageView) holder.itemView.findViewById(R.id.collectComicThumb);
        TextView collectComicTitle= (TextView) holder.itemView.findViewById(R.id.collectComicTitle);
        TextView collectNewEpisode= (TextView) holder.itemView.findViewById(R.id.collectNewEpisode);
        TextView isComicUpdate= (TextView) holder.itemView.findViewById(R.id.isComicUpdate);
        collectComicThumb.setImageResource(mEntityList.get(position).getCollectThumb());
        collectComicTitle.setText(mEntityList.get(position).getCollectTitle());
        collectNewEpisode.setText(String.valueOf(mEntityList.get(position).getCollectNewEpisode()));
        if(mEntityList.get(position).collectIsUpdate())
        {
            isComicUpdate.setText("有更新");
        }
        else
        {
            isComicUpdate.setText("没更新");
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
