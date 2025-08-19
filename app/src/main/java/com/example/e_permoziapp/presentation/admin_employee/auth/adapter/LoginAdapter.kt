package com.example.e_permoziapp.presentation.admin_employee.auth.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.e_permoziapp.presentation.admin_employee.auth.ui.LoginFragment

class LoginAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    private val roleList = listOf("admin", "employee")
    override fun getItemCount(): Int = roleList.size

    override fun createFragment(position: Int): Fragment {
        return LoginFragment.newInstance(roleList[position])
    }
}