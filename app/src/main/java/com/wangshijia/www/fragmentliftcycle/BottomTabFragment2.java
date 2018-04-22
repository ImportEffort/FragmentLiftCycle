package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wangshijia.www.fragmentliftcycle.inner.Bottom2InnerFragment1;
import com.wangshijia.www.fragmentliftcycle.inner.Bottom2InnerFragment2;
import com.wangshijia.www.fragmentliftcycle.inner.Bottom2InnerFragment3;
import com.wangshijia.www.fragmentliftcycle.inner.Bottom2InnerFragment4;

import java.util.ArrayList;

public class BottomTabFragment2 extends LazyLoadBaseFragment {

    private String text = getClass().getSimpleName() + " ";
    public static final String TAG = "Fragment";
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public static BottomTabFragment2 newInstance(String params) {
        BottomTabFragment2 fragment = new BottomTabFragment2();
        Bundle args = new Bundle();
        args.putString("params",params);
        fragment.setArguments(args);
        return fragment;
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
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        initViewPager();
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Bottom2InnerFragment1());
        fragments.add(new Bottom2InnerFragment2());
        fragments.add(new Bottom2InnerFragment3());
        fragments.add(new Bottom2InnerFragment4());

        String[] titles = {"Tab1", "Tab2", "Tab3", "Tab4"};

        viewPager.setAdapter(new TestPagerPagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager, false);
    }
}
