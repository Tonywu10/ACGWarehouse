package com.example.tonyw.acgwarehouse.activity;

import android.app.Activity;
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
 * Created by tonywu10 on 2016/12/12.
 */

public class CollectActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    public static Activity collectActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        collectActivity=this;
        mViewPager= (ViewPager) findViewById(R.id.vp);
        mTabLayout= (TabLayout) findViewById(R.id.tl);
        mToolbar= (Toolbar) findViewById(R.id.collect_toolbar);

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
