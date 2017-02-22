package com.example.tonyw.acgwarehouse.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tonyw.acgwarehouse.fragments.AnimateFragment;
import com.example.tonyw.acgwarehouse.fragments.NewsFragment;

import java.util.ArrayList;

/**
 * Created by tonywu10 on 2016/11/28.
 */

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<String> titleContainer=new ArrayList<>();

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = new AnimateFragment();
        }
        if (position==1)
        {
            f=new NewsFragment();
        }
        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        titleContainer.add("动画");
        titleContainer.add("资讯");
        return titleContainer.get(position);
    }
}