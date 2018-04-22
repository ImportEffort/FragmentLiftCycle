package com.wangshijia.www.fragmentliftcycle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseLifeCircleFragment extends Fragment {

    public static final String TAG =  "BaseLifeCircleFragment";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        LogUtils.i(TAG,getClass().getSimpleName() + "  onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LogUtils.i(TAG, getClass().getSimpleName() + "  onCreate getParentFragment  " + (getParentFragment() == null));
//        LogUtils.i(TAG,getClass().getSimpleName() + "  onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtils.i(TAG,getClass().getSimpleName() + "  onCreateView");
//        LogUtils.i(TAG, getClass().getSimpleName() + "  onCreateView getParentFragment  " + (getParentFragment() == null));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
//        LogUtils.i(TAG,getClass().getSimpleName() + "  onAttachFragment");
//        LogUtils.i(TAG, getClass().getSimpleName() + "  onAttachFragment getParentFragment  " + (getParentFragment() == null));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.i(TAG,getClass().getSimpleName() + "  setUserVisibleHint " + isVisibleToUser);
//        LogUtils.i(TAG,getClass().getSimpleName() + "  isResumed() " + isResumed());
//        LogUtils.i(TAG,getClass().getSimpleName() + "  isAdded() " + isAdded());
//        LogUtils.i(TAG, getClass().getSimpleName() + "  setUserVisibleHint getParentFragment != null  " + (getParentFragment() != null));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.i(TAG,getClass().getSimpleName() + "  onHiddenChanged " + hidden);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.i(TAG,getClass().getSimpleName() + "  onActivityCreated ");
//        LogUtils.i(TAG, getClass().getSimpleName() + "  onActivityCreated getParentFragment != null  " + (getParentFragment() != null));
    }

    @Override
    public void onStart() {
        super.onStart();
//        LogUtils.i(TAG,getClass().getSimpleName() + "  onStart ");
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(TAG,getClass().getSimpleName() + " onResume  ");
//        LogUtils.i(TAG,getClass().getSimpleName() + "   fragment.getUserVisibleHint() = "  + getUserVisibleHint());
//        LogUtils.i(TAG, getClass().getSimpleName() + "  onResume getParentFragment != null  " + (getParentFragment() != null));

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(TAG,getClass().getSimpleName() + "  onPause ");
//        LogUtils.i(TAG,getClass().getSimpleName() + "   fragment.getUserVisibleHint() = "  + getUserVisibleHint());
//        LogUtils.i(TAG, getClass().getSimpleName() + "  onPause getParentFragment != null  " + (getParentFragment() != null));

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i(TAG,getClass().getSimpleName() + "  onStop ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i(TAG,getClass().getSimpleName() + "  onDestroyView ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(TAG,getClass().getSimpleName() + "  onDestroy ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.i(TAG,getClass().getSimpleName() + "  onDetach ");
    }
}
