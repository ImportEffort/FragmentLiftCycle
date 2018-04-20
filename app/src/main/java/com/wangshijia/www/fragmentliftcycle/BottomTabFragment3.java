package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BottomTabFragment3 extends LazyLoadBaseFragment {

    private String text = getClass().getSimpleName() + " ";

    public static BottomTabFragment3 newInstance(String params) {
        BottomTabFragment3 fragment = new BottomTabFragment3();
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
}
