package com.example.tonyw.acgwarehouse.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.utils.BaseHolder;
import com.example.tonyw.acgwarehouse.utils.Entity;

import java.util.List;

public class DownloadingAdapter extends RecyclerView.Adapter<BaseHolder> {
    private List<Entity> mEntityList;
    public DownloadingAdapter(List<Entity> entityList)
    {
        mEntityList=entityList;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_downloading_item, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        ImageView downloadingThumb= (ImageView) holder.itemView.findViewById(R.id.downloadingThumb);
        TextView downloadingTitle= (TextView) holder.itemView.findViewById(R.id.downloadingTitle);
        ProgressBar downloadingProgress= (ProgressBar) holder.itemView.findViewById(R.id.downloadingProgressbar);
        TextView downloadingSize= (TextView) holder.itemView.findViewById(R.id.downloadingSize);
        downloadingThumb.setImageResource(mEntityList.get(position).getDownloadingThumb());
        downloadingTitle.setText(mEntityList.get(position).getDownloadingTitle());
        downloadingProgress.setProgress(mEntityList.get(position).getDownloadingProgress());
        downloadingSize.setText(String.valueOf(mEntityList.get(position).getDownloadingSize())+"MB"+"/"+String.valueOf(mEntityList.get(position).getDownloadingSize())+"MB");
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
