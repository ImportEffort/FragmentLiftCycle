package com.wangshijia.www.fragmentliftcycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private BottomNavigationView navigation;
    private ViewPager viewPager;

    private BottomTabFragment1 fragment1 = BottomTabFragment1.newInstance("渣渣辉");
    private BottomTabFragment2 fragment2 = BottomTabFragment2.newInstance("古天乐");
    private BottomTabFragment3 fragment3 = BottomTabFragment3.newInstance("游戏");
    private BottomTabFragment4 fragment4 = BottomTabFragment4.newInstance("贪玩蓝月");
    ArrayList<Fragment> fragments = new ArrayList<>(4);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.floatingButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToFragmentHideShowActivity();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //添加viewPager事件监听（很容易忘）
        viewPager.addOnPageChangeListener(this);
        navigation = (BottomNavigationView) findViewById(R.id.design_bottom_sheet);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.setAdapter(new TestPagerPagerAdapter(getSupportFragmentManager(), fragments, null));
//        viewPager.setAdapter(new TestPagerPageStateAdapter(getSupportFragmentManager(), fragments, null));
//        viewPager.setOffscreenPageLimit(4);
    }

    private void moveToFragmentHideShowActivity() {
        Intent intent = new Intent(this, FragmentHideShowActivity.class);
        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //点击BottomNavigationView的Item项，切换ViewPager页面
            //menu/navigation.xml里加的android:orderInCategory属性就是下面item.getOrder()取的值
            viewPager.setCurrentItem(item.getOrder());
            return true;
        }

    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //页面滑动的时候，改变BottomNavigationView的Item高亮
        navigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
