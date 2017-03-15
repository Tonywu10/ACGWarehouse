package com.example.tonyw.acgwarehouse.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tonyw.acgwarehouse.fragments.CollectNewsFragment;

import java.util.ArrayList;

public class CollectViewPagerAdapter extends FragmentStatePagerAdapter{
    private ArrayList<String> titleContainer=new ArrayList<>();
    public CollectViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = new CollectNewsFragment();
        }
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        titleContainer.add("资讯");
        return titleContainer.get(position);
    }

    @Override
    public int getCount() {
        return 1;
    }
}
