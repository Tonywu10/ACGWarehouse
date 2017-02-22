package com.example.tonyw.acgwarehouse.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.DownloadViewPagerAdapter;


/**
 * Created by tonywu10 on 2016/12/10.
 */

public class DownloadActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ViewPager mViewPager;
        TabLayout mTabLayout;
        Toolbar mToolbar;

        mViewPager= (ViewPager) findViewById(R.id.vp);
        mTabLayout= (TabLayout) findViewById(R.id.tl);
        mToolbar= (Toolbar) findViewById(R.id.download_toolbar);

        DownloadViewPagerAdapter adapter=new DownloadViewPagerAdapter(getSupportFragmentManager());
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
        getMenuInflater().inflate(R.menu.download_toolbar_menu, menu);
        return true;
    }
}
