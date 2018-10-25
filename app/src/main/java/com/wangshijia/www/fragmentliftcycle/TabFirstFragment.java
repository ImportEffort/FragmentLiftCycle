package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class TabFragment extends LazyLoadBaseFragment {

    private String text;

    public static TabFragment newInstance(String params) {
        TabFragment fragment = new TabFragment();
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
        TextView textView = rootView.findViewById(R.id.text);
        textView.setText(text);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

    }
}
