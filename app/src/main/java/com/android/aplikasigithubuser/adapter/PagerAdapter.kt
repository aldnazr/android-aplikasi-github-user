package com.android.aplikasigithubuser.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.aplikasigithubuser.ui.FollowerFragment
import com.android.aplikasigithubuser.ui.FollowingFragment

class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    val fragments = listOf(FollowerFragment(), FollowingFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}