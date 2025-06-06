package com.example.e_permoziapp.presentation.user.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.e_permoziapp.presentation.user.profile.ui.ProfilePengajuanFragment

class PengajuanFragmentAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    val statues = listOf("process", "success", "failed")
    override fun getItemCount(): Int  = statues.size

    override fun createFragment(position: Int): Fragment {
        return ProfilePengajuanFragment.newInstance(statues[position])
    }
}