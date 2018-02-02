package com.wangshijia.www.fragmentliftcycle.inner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangshijia.www.fragmentliftcycle.LazyLoadBaseFragment;
import com.wangshijia.www.fragmentliftcycle.R;

/**
 * 当 FragmentAdapter 嵌套的 Fragment 中仍有 FragmentAdapter ，当承载内部 Adapter 的 Fragment 走生命周期函数的时候
 * 内部Fragment 也将会紧跟外部的 Fragment 生命周期函数调用自己的 Fragment 生命周期函数
 * 如 外部 TestFragment2 承载了 FragmentAdapter Activity 初始化外部 TestFragment1 完成后，TestFragment2将被预加载，
 * 而TestFragment2中的InnerTestFragment1 将
 */
public class InnerTestFragment4 extends LazyLoadBaseFragment {

    private TextView textView;
    private String text = getClass().getSimpleName() + " ";
    public static final String TAG = "Fragment";


    public InnerTestFragment4() {
        // Required empty public constructor
    }

    public static InnerTestFragment4 newInstance(String params) {
        InnerTestFragment4 fragment = new InnerTestFragment4();
        Bundle args = new Bundle();
        args.putString("params", params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            text = getArguments().getString("params");
        }

        Log.i(TAG, text + "onCreate");

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.i(TAG, text + "setUserVisibleHint  " + isVisibleToUser + "parentUserVisibleHint ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, text + "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, text + "onActivityCreated");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, text + "onViewCreated");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, text + "onAttach");
        Fragment parentFragment = getParentFragment();
        Log.i(TAG, text + "parentUserVisibleHint " + parentFragment.getUserVisibleHint());

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, text + "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, text + "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, text + "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, text + "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, text + "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, text + "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, text + "onDestroy");
    }
}
