package com.wangshijia.www.fragmentliftcycle.dayFragmentDemo

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.wangshijia.www.fragmentliftcycle.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.*

class Main2Activity : AppCompatActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private lateinit var fragments: LinkedList<LazyLoadBaseFragment>
    private lateinit var titles: LinkedList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initDefaultFragments()

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        container.adapter = mSectionsPagerAdapter

        tabLayout.setupWithViewPager(container)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        tabLayout.postDelayed(object : Runnable {
            override fun run() {
                initAddFragments()
                mSectionsPagerAdapter!!.notifyDataSetChanged()
            }
        }, 4000)

        add.setOnClickListener { addFragment() }
        delete.setOnClickListener { deleteFragment() }
    }

    private fun initDefaultFragments() {
        fragments = LinkedList()
        fragments.add(BottomTabFragment2.newInstance("OuterFragment2"))
        fragments.add(BottomTabFragment1.newInstance("OuterFragment1"))
        fragments.add(BottomTabFragment3.newInstance("OuterFragment3"))
        fragments.add(BottomTabFragment4.newInstance("OuterFragment4"))
        titles = LinkedList()
        titles.add("tab1")
        titles.add("tab2")
        titles.add("tab3")
        titles.add("tab4")
    }

    private fun initAddFragments() {
        fragments.add(BottomTabFragment2.newInstance("OuterFragment6"))
        fragments.add(BottomTabFragment1.newInstance("OuterFragment5"))
        fragments.add(BottomTabFragment3.newInstance("OuterFragment7"))
        fragments.add(BottomTabFragment4.newInstance("OuterFragment8"))
        titles.add("newTab1")
        titles.add("newTab2")
        titles.add("newTab3")
        titles.add("newTab4")
    }

    private var num = 5
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

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

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
