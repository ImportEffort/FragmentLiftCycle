package com.wangshijia.www.fragmentliftcycle.hideshow;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wangshijia.www.fragmentliftcycle.LazyLoadBaseFragment;
import com.wangshijia.www.fragmentliftcycle.R;
import com.wangshijia.www.fragmentliftcycle.TestPagerPageStateAdapter;
import com.wangshijia.www.fragmentliftcycle.inner.hideshow.Bottom2HideShowInnerFragment1;
import com.wangshijia.www.fragmentliftcycle.inner.hideshow.Bottom2HideShowInnerFragment2;
import com.wangshijia.www.fragmentliftcycle.inner.hideshow.Bottom2HideShowInnerFragment3;
import com.wangshijia.www.fragmentliftcycle.inner.hideshow.Bottom2HideShowInnerFragment4;

import java.util.ArrayList;

public class HideShowTabFragment2 extends LazyLoadBaseFragment {

    private String text = getClass().getSimpleName() + " ";
    public static final String TAG = "Fragment";
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public HideShowTabFragment2() {
        // Required empty public constructor
    }


    public static HideShowTabFragment2 newInstance(String params) {
        HideShowTabFragment2 fragment = new HideShowTabFragment2();
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

        ArrayList<Fragment> fragments = new ArrayList<>(4);
        fragments.add(new Bottom2HideShowInnerFragment1());
        fragments.add(new Bottom2HideShowInnerFragment2());
        fragments.add(new Bottom2HideShowInnerFragment3());
        fragments.add(new Bottom2HideShowInnerFragment4());

        String[] titles = {"Tab1", "Tab2", "Tab3", "Tab4"};

        viewPager.setAdapter(new TestPagerPageStateAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager, false);
    }
}
