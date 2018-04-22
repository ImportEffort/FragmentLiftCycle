package com.wangshijia.www.fragmentliftcycle.inner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wangshijia.www.fragmentliftcycle.LazyLoadBaseFragment;
import com.wangshijia.www.fragmentliftcycle.R;
import com.wangshijia.www.fragmentliftcycle.TestPagerPagerAdapter;
import com.wangshijia.www.fragmentliftcycle.inner.innerHideShow.ThirdInnerFragment.ThirdInnerTabFragment1;
import com.wangshijia.www.fragmentliftcycle.inner.innerHideShow.ThirdInnerFragment.ThirdInnerTabFragment2;
import com.wangshijia.www.fragmentliftcycle.inner.innerHideShow.ThirdInnerFragment.ThirdInnerTabFragment3;
import com.wangshijia.www.fragmentliftcycle.inner.innerHideShow.ThirdInnerFragment.ThirdInnerTabFragment4;

import java.util.ArrayList;

/**
 * 当 FragmentAdapter 嵌套的 Fragment 中仍有 FragmentAdapter ，当承载内部 Adapter 的 Fragment 走生命周期函数的时候
 * 内部Fragment 也将会紧跟外部的 Fragment 生命周期函数调用自己的 Fragment 生命周期函数
 * 如 外部 TestFragment2 承载了 FragmentAdapter Activity 初始化外部 TestFragment1 完成后，TestFragment2将被预加载，
 * 而TestFragment2中的InnerTestFragment1 将
 */
public class Bottom2InnerFragment1 extends LazyLoadBaseFragment {

    private String text = getClass().getSimpleName() + " ";

    public static Bottom2InnerFragment1 newInstance(String params) {
        Bottom2InnerFragment1 fragment = new Bottom2InnerFragment1();
        Bundle args = new Bundle();
        args.putString("params", params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }


    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void initView(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        initViewPager();
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ThirdInnerTabFragment1());
        fragments.add(new ThirdInnerTabFragment2());
        fragments.add(new ThirdInnerTabFragment3());
        fragments.add(new ThirdInnerTabFragment4());

        String[] titles = {"Tab1", "Tab2", "Tab3", "Tab4"};

        viewPager.setAdapter(new TestPagerPagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager, false);
    }
}
