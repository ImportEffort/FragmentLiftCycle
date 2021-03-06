package com.wangshijia.www.fragmentliftcycle.hideshow;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wangshijia.www.fragmentliftcycle.LazyLoadBaseFragment;
import com.wangshijia.www.fragmentliftcycle.R;

public class HideShowTabFragment1 extends LazyLoadBaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String text = getClass().getSimpleName() + " ";


    public static HideShowTabFragment1 newInstance(String params) {
        HideShowTabFragment1 fragment = new HideShowTabFragment1();
        Bundle args = new Bundle();
        args.putString("params",params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView(View rootView) {
        TextView textView = rootView.findViewById(R.id.text);
        textView.setText(text);
    }


//    @Override
//    protected int getLayoutRes() {
//        return R.layout.fragment_home;
//    }
//
//    @Override
//    protected void initView(View view) {
//        viewPager = view.findViewById(R.id.viewPager);
//        tabLayout = view.findViewById(R.id.tabLayout);
//        initViewPager();
//    }
//
//    private void initViewPager() {
//        ArrayList<Fragment> fragments = new ArrayList<>();
//        fragments.add(new Bottom1InnerFragment1());
//        fragments.add(new Bottom1InnerFragment2());
//        fragments.add(new Bottom1InnerFragment3());
//        fragments.add(new Bottom1InnerFragment4());
//
//        String[] titles = {"Tab1", "Tab2", "Tab3", "Tab4"};
//
//        viewPager.setAdapter(new TestPagerAdapter(getChildFragmentManager(), fragments, titles));
//        tabLayout.setupWithViewPager(viewPager, false);
//    }
}
