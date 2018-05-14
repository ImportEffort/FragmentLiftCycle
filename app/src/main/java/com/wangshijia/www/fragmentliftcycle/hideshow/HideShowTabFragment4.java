package com.wangshijia.www.fragmentliftcycle.hideshow;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.wangshijia.www.fragmentliftcycle.IIInnerFragment;
import com.wangshijia.www.fragmentliftcycle.IInnerFragment;
import com.wangshijia.www.fragmentliftcycle.LazyLoadBaseFragment;
import com.wangshijia.www.fragmentliftcycle.R;

import java.util.ArrayList;

public class HideShowTabFragment4 extends LazyLoadBaseFragment {

    private TextView textView;
    private String text = getClass().getSimpleName() + " ";
    private TabLayout tabLayout;
    private ArrayList<LazyLoadBaseFragment> fragments;

    public static HideShowTabFragment4 newInstance(String params) {
        HideShowTabFragment4 fragment = new HideShowTabFragment4();
        Bundle args = new Bundle();
        args.putString("params", params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_inner_hide_show;
    }

    @Override
    protected void initView(View rootView) {
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragments = new ArrayList<>();
        fragments.add(IIInnerFragment.newInstance("1kjshkjdsh"));
        fragments.add(IInnerFragment.newInstance("djjdsdsdss"));
    }

    private LazyLoadBaseFragment currentFragment;
    private void showFragment(int position) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (currentFragment != null){
            transaction.hide(currentFragment);
        }
        LazyLoadBaseFragment currentFragment = fragments.get(position);
        if (currentFragment != null && !currentFragment.isAdded()) {
            transaction.add(R.id.fragmentContent, currentFragment, String.valueOf(position));
            //第三个参数为添加当前的fragment时绑定一个tag
        } else {
            transaction.show(currentFragment);
        }
        this.currentFragment = currentFragment;
        transaction.commitAllowingStateLoss();
    }

}
