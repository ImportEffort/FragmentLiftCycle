package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wangshijia.www.fragmentliftcycle.hideshow.HideShowTabFragment1;
import com.wangshijia.www.fragmentliftcycle.hideshow.HideShowTabFragment2;
import com.wangshijia.www.fragmentliftcycle.hideshow.HideShowTabFragment3;
import com.wangshijia.www.fragmentliftcycle.hideshow.HideShowTabFragment4;

public class FragmentHideShowActivity extends AppCompatActivity {


    private HideShowTabFragment1 fragment1;
    private HideShowTabFragment2 fragment2;
    private HideShowTabFragment3 fragment3;
    private HideShowTabFragment4 fragment4;

    private String[] content = {"渣渣辉", "古天乐", "游戏", "贪玩蓝月"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_hide_show);

        BottomNavigationView navigation = findViewById(R.id.design_bottom_sheet);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showTargetFragment(0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int pos = item.getOrder();
            showFragment(pos);
            return true;
        }

    };

    private void showFragment(int pos) {
        hideFragments();
        showTargetFragment(pos);
    }

    private void showTargetFragment(int pos) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (pos) {
            case 0:
                if (fragment1 == null) {
                    fragment1 = HideShowTabFragment1.newInstance(content[pos]);
                    transaction.add(R.id.fragmentContent, fragment1, String.valueOf(pos));
                } else {
                    transaction.show(fragment1);
                }
                break;
            case 1:
                if (fragment2 == null) {
                    fragment2 = HideShowTabFragment2.newInstance(content[pos]);
                    transaction.add(R.id.fragmentContent, fragment2, String.valueOf(pos));
                } else {
                    transaction.show(fragment2);
                }
                break;
            case 2:
                if (fragment3 == null) {
                    fragment3 = HideShowTabFragment3.newInstance(content[pos]);
                    transaction.add(R.id.fragmentContent, fragment3, String.valueOf(pos));
                } else {
                    transaction.show(fragment3);
                }
                break;
            case 3:
                if (fragment4 == null) {
                    fragment4 = HideShowTabFragment4.newInstance(content[pos]);
                    transaction.add(R.id.fragmentContent, fragment4, String.valueOf(pos));
                } else {
                    transaction.show(fragment4);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment1 != null) {
            fragmentTransaction.hide(fragment1);
        }

        if (fragment2 != null) {
            fragmentTransaction.hide(fragment2);
        }

        if (fragment3 != null) {
            fragmentTransaction.hide(fragment3);
        }

        if (fragment4 != null) {
            fragmentTransaction.hide(fragment4);
        }

        fragmentTransaction.commit();

    }
}
