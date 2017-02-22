package com.example.tonyw.acgwarehouse.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tonyw.acgwarehouse.fragments.CollectAnimateFragment;
import com.example.tonyw.acgwarehouse.fragments.CollectComicFragment;
import com.example.tonyw.acgwarehouse.fragments.CollectNewsFragment;

import java.util.ArrayList;

/**
 * Created by tonywu10 on 2016/12/12.
 */

public class CollectViewPagerAdapter extends FragmentStatePagerAdapter{
    ArrayList<String> titleContainer=new ArrayList<>();
    public CollectViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = new CollectAnimateFragment();
        }
        if (position == 1) {
            f = new CollectComicFragment();
        }
        if (position == 2) {
            f = new CollectNewsFragment();
        }
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        titleContainer.add("动画");
        titleContainer.add("漫画");
        titleContainer.add("资讯");
        return titleContainer.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
