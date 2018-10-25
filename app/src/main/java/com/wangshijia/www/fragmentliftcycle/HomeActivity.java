package com.wangshijia.www.fragmentliftcycle;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<LazyLoadBaseFragment> fragments = new ArrayList<>();
    private LazyLoadBaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        View tab1 = findViewById(R.id.tab1);
        View tab2 = findViewById(R.id.tab2);
        View tab3 = findViewById(R.id.tab3);
        View tab4 = findViewById(R.id.tab4);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);

        initFragment();
    }

    private void initFragment() {
         BottomTabFragment1 fragment1 = BottomTabFragment1.newInstance("渣渣辉");
         BottomTabFragment2 fragment2 = BottomTabFragment2.newInstance("古天乐");
         BottomTabFragment3 fragment3 = BottomTabFragment3.newInstance("游戏");
         BottomTabFragment4 fragment4 = BottomTabFragment4.newInstance("贪玩蓝月");

        fragments.add(fragment2);
        fragments.add(fragment1);
        fragments.add(fragment3);
        fragments.add(fragment4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab1:
                showFragment(0);
                break;
            case R.id.tab2:
                showFragment(1);
                break;
            case R.id.tab3:
                showFragment(2);
                break;
            case R.id.tab4:
                showFragment(3);
                break;
        }
    }

    private void showFragment(int index){

        showFragment(String.valueOf(index+186),index);
    }

    private void showFragment(String id, int index) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //判断当前的Fragment是否为空，不为空则隐藏
        if (null != currentFragment) {
            transaction.hide(currentFragment).commit();
            transaction = getSupportFragmentManager().beginTransaction();
        }

        //如果之前没有添加过
        if (!fragments.isEmpty()) {
            LazyLoadBaseFragment currentFragment = fragments.get(index);
            if (currentFragment == null || !currentFragment.isAdded()) {
                transaction.add(R.id.contentFragment, currentFragment, String.valueOf(index));
            } else {
                transaction.show(currentFragment);
            }
            this.currentFragment = currentFragment;
            transaction.commit();
        }
    }

}
