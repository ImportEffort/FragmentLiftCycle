package com.wangshijia.www.fragmentliftcycle.hideshow;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wangshijia.www.fragmentliftcycle.LazyLoadBaseFragment;
import com.wangshijia.www.fragmentliftcycle.R;

public class HideShowTabFragment3 extends LazyLoadBaseFragment {

    private String text = getClass().getSimpleName() + " ";

    public static HideShowTabFragment3 newInstance(String params) {
        HideShowTabFragment3 fragment = new HideShowTabFragment3();
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
