package com.example.tonyw.acgwarehouse.adapters;

import android.support.v7.widget.LinearLayoutManager;
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
 * Created by tonywu10 on 2016/12/10.
 */

public class DownloadedAdapter extends RecyclerView.Adapter<BaseHolder> {
    private List<Entity> mEntityList;
    public DownloadedAdapter(List<Entity> entityList, LinearLayoutManager linearLayoutManager)
    {
        mEntityList=entityList;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_downloaded_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        ImageView downloadedThumb= (ImageView) holder.itemView.findViewById(R.id.downloadedThumb);
        TextView downloadedTitle= (TextView) holder.itemView.findViewById(R.id.downloadedTitle);
        TextView downloadedSize= (TextView) holder.itemView.findViewById(R.id.downloadedSize);
        TextView downloadedType= (TextView) holder.itemView.findViewById(R.id.downloadedType);
        downloadedThumb.setImageResource(mEntityList.get(position).getDownloadedThumb());
        downloadedTitle.setText(mEntityList.get(position).getDownloadedTitle());
        downloadedSize.setText(String.valueOf(mEntityList.get(position).getDownloadedSize()));
        downloadedType.setText(mEntityList.get(position).getDownloadedType());
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
