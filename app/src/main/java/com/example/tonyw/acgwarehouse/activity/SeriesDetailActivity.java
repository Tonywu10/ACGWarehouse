package com.example.tonyw.acgwarehouse.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.SeriesDetailAdapter;
import com.example.tonyw.acgwarehouse.entity.SeriesDetailEntity;
import com.example.tonyw.acgwarehouse.utils.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.DEFAULT_SPAN_COUNT_FIVE;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.IS_FINISH;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

public class SeriesDetailActivity extends AppCompatActivity{
    private TextView mIntro;
    private RecyclerView mRecyclerView;
    public String result="";
    private List<Entity> entityData=new ArrayList<>();
    private SeriesDetailAdapter mSeriesDetailAdapter;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case IS_FINISH:
                    try {
                        Intent it=getIntent();
                        mCollapsingToolbarLayout.setTitle(it.getStringExtra("videoTitle"));
                        mRecyclerView.setNestedScrollingEnabled(false);
                        JSONArray jsonArray=new JSONArray(result);
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            String videoId=jsonObject.getString("VideoId");
                            mSeriesDetailAdapter.addItem(new SeriesDetailEntity(i+1,videoId));
                        }
                        mRecyclerView.setAdapter(mSeriesDetailAdapter);
                        mIntro.setMinimumHeight(300);
                        mIntro.setText("\u3000\u3000"+it.getStringExtra("videoIntro"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);
        mRecyclerView= (RecyclerView)findViewById(R.id.series_detail_recyclerview);
        mIntro= (TextView) findViewById(R.id.series_introduction);
        WrappingGridLayoutManager gridLayoutManager=new WrappingGridLayoutManager(getApplicationContext(),DEFAULT_SPAN_COUNT_FIVE);
        mCollapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.requestDisallowInterceptTouchEvent(false);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        new getJson().start();
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        mSeriesDetailAdapter =new SeriesDetailAdapter(entityData,DEFAULT_SPAN_COUNT_FIVE,this);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.series_detail_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class getJson extends Thread{
        @Override
        public void run() {
            Intent it=getIntent();
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/VideoItemDemo?parent_url="+it.getStringExtra("videoUrl");
            try {
                result=getJsonData(path);
                sendMessage(mHandler,IS_FINISH);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class WrappingGridLayoutManager extends GridLayoutManager
    {

        private int[] mMeasuredDimension = new int[2];
        private WrappingGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }
        @Override
        public boolean canScrollVertically() {
            return false;
        }
        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,int widthSpec, int heightSpec) {
            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);
            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i=i+5) {
                if (getOrientation() == HORIZONTAL) {
                    measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), heightSpec, mMeasuredDimension);
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    measureScrapChild(recycler, i, widthSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }
            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }
            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec, int heightSpec, int[] measuredDimension) {
            View view = recycler.getViewForPosition(position);
            if (view.getVisibility() == View.GONE) {
                measuredDimension[0] = 0;
                measuredDimension[1] = 0;
                return;
            }
            // For adding Item Decor Insets to view
            super.measureChildWithMargins(view, 0, 0);
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight() + getDecoratedLeft(view) + getDecoratedRight(view), p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom() + getDecoratedTop(view) + getDecoratedBottom(view), p.height);
            view.measure(childWidthSpec, childHeightSpec);
            // Get decorated measurements
            measuredDimension[0] = getDecoratedMeasuredWidth(view) + p.leftMargin + p.rightMargin;
            measuredDimension[1] = getDecoratedMeasuredHeight(view) + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }
}
