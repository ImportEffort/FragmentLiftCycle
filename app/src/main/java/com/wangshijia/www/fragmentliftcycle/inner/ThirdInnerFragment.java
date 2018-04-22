package com.wangshijia.www.fragmentliftcycle.inner;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wangshijia.www.fragmentliftcycle.LazyLoadBaseFragment;
import com.wangshijia.www.fragmentliftcycle.R;

/**
 */
public class ThirdInnerFragment extends LazyLoadBaseFragment {


    private String text = getClass().getSimpleName() + " ";

    public static ThirdInnerFragment newInstance(String params) {
        ThirdInnerFragment fragment = new ThirdInnerFragment();
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
        TextView textView = rootView.findViewById(R.id.text);
        textView.setText(text);
    }
}
