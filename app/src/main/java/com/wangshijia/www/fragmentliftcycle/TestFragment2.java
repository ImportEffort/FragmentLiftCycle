package com.wangshijia.www.fragmentliftcycle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangshijia.www.fragmentliftcycle.inner.InnerTestFragment1;
import com.wangshijia.www.fragmentliftcycle.inner.InnerTestFragment2;
import com.wangshijia.www.fragmentliftcycle.inner.InnerTestFragment3;
import com.wangshijia.www.fragmentliftcycle.inner.InnerTestFragment4;

import java.util.ArrayList;

//public class TestFragment2 extends Fragment {
public class TestFragment2 extends LazyLoadBaseFragment {

    private String text = getClass().getSimpleName() + " ";
    public static final String TAG = "Fragment";
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public TestFragment2() {
        // Required empty public constructor
    }

    public static TestFragment2 newInstance(String params) {
        TestFragment2 fragment = new TestFragment2();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, text + "onActivityCreated");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, text + "setUserVisibleHint  " + isVisibleToUser);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, text + "onCreateView");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        initViewPager();
    }

    private void initViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new InnerTestFragment1());
        fragments.add(new InnerTestFragment2());
        fragments.add(new InnerTestFragment3());
        fragments.add(new InnerTestFragment4());

        String[] titles = {"古天乐", "渣渣辉", "贪玩蓝月", "这款游戏"};

        viewPager.setAdapter(new TestPagerAdapter(getChildFragmentManager(), fragments, titles));
        tabLayout.setupWithViewPager(viewPager, false);
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
