package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class BottomTabFragment1 extends LazyLoadBaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String text = getClass().getSimpleName() + " ";


    public static BottomTabFragment1 newInstance(String params) {
        BottomTabFragment1 fragment = new BottomTabFragment1();
        Bundle args = new Bundle();
        args.putString("params", params);
        fragment.setArguments(args);
        return fragment;
    }

    public String getText() {
        return text;
    }

//    @Override
//    protected int getLayoutRes() {
//        return R.layout.fragment_test;
//    }
//
//    @Override
//    protected void initView(View rootView) {
//        TextView textView = rootView.findViewById(R.id.text);
//        textView.setText(text);
//    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
    }

    @Override
    public void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initViewPager();
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(TabFirstFragment.newInstance("tab1"));
        fragments.add(TabFragment.newInstance("tab2"));
        fragments.add(TabFragment.newInstance("tab3"));
        fragments.add(TabFragment.newInstance("tab4"));


        String[] titles = {"Tab1", "Tab2", "Tab3", "Tab4"};

        viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), titles,fragments));
        tabLayout.setupWithViewPager(viewPager, false);
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] titles;
        private final ArrayList<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm, String[] titles, ArrayList<Fragment> fragments) {
            super(fm);
            this.titles = titles;
            this.fragments =fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment f = (Fragment) super.instantiateItem(container, position);
            return f;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
