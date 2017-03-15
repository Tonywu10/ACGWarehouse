package com.example.tonyw.acgwarehouse.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.CollectViewPagerAdapter;

/**
 *收藏新闻模块
 */
public class CollectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ViewPager mViewPager= (ViewPager) findViewById(R.id.vp);
        TabLayout mTabLayout= (TabLayout) findViewById(R.id.tl);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.collect_toolbar);
        CollectViewPagerAdapter adapter=new CollectViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.collect_toolbar_menu, menu);
        return true;
    }
}
