package com.wangshijia.www.fragmentliftcycle

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import java.util.*

class TestFragment : LazyLoadBaseFragment() {

    private val fragments: LinkedList<LazyLoadBaseFragment> = LinkedList()
    private val titles: LinkedList<String> = LinkedList()
    private var outerName = javaClass.simpleName

    companion object {
        private val ARG_NAME = "outer_name"
        fun newInstance(caught: String): TestFragment {
            val args = Bundle()
            args.putSerializable(ARG_NAME, caught)
            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putString("name",outerName)
    }



    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun initView(rootView: View) {
        val viewPager1 = rootView.findViewById<ViewPager>(R.id.viewPager)
        val tabLayout1 = rootView.findViewById<TabLayout>(R.id.tabLayout)

        outerName = arguments?.getString(ARG_NAME)
        for (index in 1..5) {
            fragments.add(IInnerFragment.newInstance("$outerName&InnerFragment", index.toString()))
            titles.add("Tab$index")
        }
        val sectionPagerAdapter = SectionPagerAdapter(childFragmentManager, fragments, titles)
        viewPager1.offscreenPageLimit = 5
        viewPager1.adapter = sectionPagerAdapter
        tabLayout1.setupWithViewPager(viewPager1)
        tabLayout1.tabMode = TabLayout.MODE_SCROLLABLE
    }


    override fun onFragmentResume() {
        LogUtils.e(outerName + "  对用户可见")

    }

    override fun onFragmentPause() {
        LogUtils.e(outerName + "  对用户不可见")

    }

    override fun onFragmentFirstVisible() {
        LogUtils.e(outerName + "  对用户第一次可见")

    }

    inner class SectionPagerAdapter(fm: FragmentManager, var fragments: LinkedList<LazyLoadBaseFragment>, var titles: LinkedList<String>) : FragmentPagerAdapter(fm) {

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
    inner class SectionStatePagerAdapter(fm: FragmentManager, var fragments: LinkedList<LazyLoadBaseFragment>, var titles: LinkedList<String>) : FragmentStatePagerAdapter(fm) {

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