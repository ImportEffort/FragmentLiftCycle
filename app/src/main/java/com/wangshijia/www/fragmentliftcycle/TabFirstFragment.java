package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class TabFirstFragment extends BaseRecyclerViewFragment {

    private String text;

    public static TabFirstFragment newInstance(String params) {
        TabFirstFragment fragment = new TabFirstFragment();
        Bundle args = new Bundle();
        args.putString("params", params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = getArguments().getString("params");
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_test;
    }

    @Override
    public void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
//        LogUtils.e("    之前   " + (recyclerView == null ));

        recyclerView.setHasFixedSize(false);
//        LogUtils.e("     之后  " + (recyclerView == null ));
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

    }
}
