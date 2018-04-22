package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wangshijia.www.fragmentliftcycle.inner.innerHideShow.InnerHideShowTabFragment1;
import com.wangshijia.www.fragmentliftcycle.inner.innerHideShow.InnerHideShowTabFragment2;
import com.wangshijia.www.fragmentliftcycle.inner.innerHideShow.InnerHideShowTabFragment3;
import com.wangshijia.www.fragmentliftcycle.inner.innerHideShow.InnerHideShowTabFragment4;

public class BottomTabFragment3 extends LazyLoadBaseFragment {
    private InnerHideShowTabFragment1 fragment1;
    private InnerHideShowTabFragment2 fragment2;
    private InnerHideShowTabFragment3 fragment3;
    private InnerHideShowTabFragment4 fragment4;

    private String[] content = {"渣渣辉", "古天乐", "游戏", "贪玩蓝月"};
    private String text = getClass().getSimpleName() + " ";


    public static BottomTabFragment3 newInstance(String params) {
        BottomTabFragment3 fragment = new BottomTabFragment3();
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


//    @Override
//    protected int getLayoutRes() {
//        return R.layout.fragment_inner_hide_show;
//    }
//
//    @Override
//    protected void initView(View rootView) {
//        TabLayout tabLayout = rootView.findViewById(R.id.tabLayout);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//                showFragment(position);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        showFragment(0);
//    }
//
//    private void showFragment(int pos) {
//        hideFragments();
//        showTargetFragment(pos);
//    }
//
//    private void showTargetFragment(int pos) {
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        switch (pos) {
//            case 0:
//                if (fragment1 == null) {
//                    fragment1 = InnerHideShowTabFragment1.newInstance(content[pos]);
//                    transaction.add(R.id.fragmentContent, fragment1, String.valueOf(pos));
//                } else {
//                    transaction.show(fragment1);
//                }
//                break;
//            case 1:
//                if (fragment2 == null) {
//                    fragment2 = InnerHideShowTabFragment2.newInstance(content[pos]);
//                    transaction.add(R.id.fragmentContent, fragment2, String.valueOf(pos));
//                } else {
//                    transaction.show(fragment2);
//                }
//                break;
//            case 2:
//                if (fragment3 == null) {
//                    fragment3 = InnerHideShowTabFragment3.newInstance(content[pos]);
//                    transaction.add(R.id.fragmentContent, fragment3, String.valueOf(pos));
//                } else {
//                    transaction.show(fragment3);
//                }
//                break;
//            case 3:
//                if (fragment4 == null) {
//                    fragment4 = InnerHideShowTabFragment4.newInstance(content[pos]);
//                    transaction.add(R.id.fragmentContent, fragment4, String.valueOf(pos));
//                } else {
//                    transaction.show(fragment4);
//                }
//                break;
//            default:
//                break;
//        }
//        transaction.commit();
//    }
//
//    private void hideFragments() {
//        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        if (fragment1 != null) {
//            fragmentTransaction.hide(fragment1);
//        }
//
//        if (fragment2 != null) {
//            fragmentTransaction.hide(fragment2);
//        }
//
//        if (fragment3 != null) {
//            fragmentTransaction.hide(fragment3);
//        }
//
//        if (fragment4 != null) {
//            fragmentTransaction.hide(fragment4);
//        }
//
//        fragmentTransaction.commit();
//
//    }
}
