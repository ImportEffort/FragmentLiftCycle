package com.wangshijia.www.fragmentliftcycle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangshijia on 2018/2/2.
 */

public class TestPagerPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;
    List<String> titles;

    public TestPagerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        if (titles != null)
            this.titles = Arrays.asList(titles);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? "" : titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
