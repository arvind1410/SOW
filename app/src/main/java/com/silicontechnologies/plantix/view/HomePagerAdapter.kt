package com.silicontechnologies.plantix.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.silicontechnologies.plantix.view.fragment.HistoryFragment
import com.silicontechnologies.plantix.view.fragment.LocateFragment
import com.silicontechnologies.plantix.view.fragment.RecentsFragment

class HomePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        if (position == 0) {
            return RecentsFragment()
        } else if (position == 1) {
            return HistoryFragment()
        } else {
            return LocateFragment()
        }

    }

    override fun getPageTitle(position: Int): CharSequence {
        if (position == 0) {
            return "Recents"
        } else if (position == 1) {
            return "History"
        } else {
            return "Locate"
        }
    }

    override fun getCount(): Int {
        return 3
    }

}