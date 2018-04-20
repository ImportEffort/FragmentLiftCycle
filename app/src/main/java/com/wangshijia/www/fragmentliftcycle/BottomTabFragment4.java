package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BottomTabFragment4 extends LazyLoadBaseFragment {

    private TextView textView;
    private String text = getClass().getSimpleName() + " ";

    public static BottomTabFragment4 newInstance(String params) {
        BottomTabFragment4 fragment = new BottomTabFragment4();
        Bundle args = new Bundle();
        args.putString("params", params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initView(View rootView) {
        textView = rootView.findViewById(R.id.text);
        textView.setText(text);
    }

}
