package com.wangshijia.www.fragmentliftcycle.dayFragmentDemo

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.wangshijia.www.fragmentliftcycle.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

class Main2Activity : AppCompatActivity() {

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private lateinit var fragments: LinkedList<LazyLoadBaseFragment>
    private lateinit var titles: LinkedList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initDefaultFragments()
//        container.offscreenPageLimit = fragments.size

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container.adapter = mSectionsPagerAdapter

        tabLayout.setupWithViewPager(container)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        tabLayout.postDelayed(object : Runnable {
            override fun run() {
                initAddFragments()
                mSectionsPagerAdapter!!.notifyDataSetChanged()
            }
        }, 5000)

        add.setOnClickListener { addFragment() }
        delete.setOnClickListener { deleteFragment() }
    }

    private fun initDefaultFragments() {
        fragments = LinkedList()
        titles = LinkedList()
        for (i in 0.. 4){
            fragments.add(TestFragment.newInstance("OuterFragment$i"))
            titles.add("Tab$i")
        }
    }

    private fun initAddFragments() {
        for (i in 5.. 8){
            fragments.add(TestFragment.newInstance("OuterFragment$i"))
            titles.add("newTab$i")
        }
        LogUtils.e("加载新 tab 完成")
        mSectionsPagerAdapter.notifyDataSetChanged()

//        container.offscreenPageLimit = fragments.size

    }

    private var num = 9
    private fun addFragment() {
        fragments.add(BottomTabFragment1.newInstance("OuterFragment$num"))
        titles.add("newTab$num")
        num++
        mSectionsPagerAdapter!!.notifyDataSetChanged()
    }

    private fun deleteFragment() {
        fragments.removeLast()
        titles.removeLast()
        num--
        mSectionsPagerAdapter!!.notifyDataSetChanged()

    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }

    inner class SectionsStatePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}
