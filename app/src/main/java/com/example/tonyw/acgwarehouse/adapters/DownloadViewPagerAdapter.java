package com.example.tonyw.acgwarehouse.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.tonyw.acgwarehouse.fragments.DownloadedFragment;
import com.example.tonyw.acgwarehouse.fragments.DownloadingFragment;

import java.util.ArrayList;

/**
 * Created by tonywu10 on 2016/12/10.
 */

public class DownloadViewPagerAdapter extends FragmentStatePagerAdapter{
    ArrayList<String> titleContainer=new ArrayList<>();
    public DownloadViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        if (position == 0) {
            f = new DownloadedFragment();
        }
        if (position == 1) {
            f = new DownloadingFragment();
        }
        return f;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        titleContainer.add("已下载");
        titleContainer.add("正在下载");
        return titleContainer.get(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
