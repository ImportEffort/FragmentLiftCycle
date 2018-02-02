package com.wangshijia.www.fragmentliftcycle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//public class TestFragment1 extends Fragment {
public class TestFragment1 extends LazyLoadBaseFragment {

    private TextView textView;
    private String text = getClass().getSimpleName() + " ";
    public static final String TAG = "Fragment";


    public TestFragment1() {
        // Required empty public constructor
    }

    public static TestFragment1 newInstance(String params) {
        TestFragment1 fragment = new TestFragment1();
        Bundle args = new Bundle();
        args.putString("params",params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            text = getArguments().getString("params");
        }

        Log.i(TAG,text + "onCreate");

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG,text + "setUserVisibleHint  " + isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,text + "onCreateView");
        return super.onCreateView(inflater,container,savedInstanceState);

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,text + "onActivityCreated");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG,text + "onViewCreated");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG,text + "onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,text + "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,text + "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,text + "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,text + "onStop");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG,text + "onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,text + "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,text + "onDestroy");
    }
}
