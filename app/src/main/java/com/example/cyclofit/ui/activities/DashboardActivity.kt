package com.example.cyclofit.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cyclofit.*
import com.example.cyclofit.ui.fragment.CommunityFragment
import com.example.cyclofit.ui.fragment.HealthFragment
import com.example.cyclofit.ui.fragment.HomeFragment
import com.example.cyclofit.ui.fragment.LeaderboardFragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar

class DashboardActivity:AppCompatActivity() {
    private var mCount:Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar?.hide()

        val homeFragment= HomeFragment()
        val healthFragment= HealthFragment()
        val communityFragment= CommunityFragment()
        val leaderboardFragment= LeaderboardFragment()

        setCurrentFragment(homeFragment)

        val myBottomNavigationView = findViewById<ChipNavigationBar>(R.id.nav_view)
        myBottomNavigationView.setItemSelected(R.id.home)
        myBottomNavigationView.setOnItemSelectedListener { item ->
            when (item) {
                R.id.home -> {
                    mCount=1
                    Log.i("NavBar", "Home pressed")
                    setCurrentFragment(homeFragment)
                }
                R.id.community -> {
                    mCount=0
                    Log.i("NavBar", "Profile pressed")
                    setCurrentFragment(communityFragment)
                }
                R.id.health -> {
                    mCount=0
                    setCurrentFragment(healthFragment)
                }
                R.id.leaderboard -> {
                    mCount=0
                    setCurrentFragment(leaderboardFragment)
                }
                else -> {
                    Log.i("NavBar", "Error?")

                }
            }
        }
    }

    override fun onBackPressed() {
        if(mCount==0) {
            goHomeFragment()
        }
        else {
            super.onBackPressed()
        }
        mCount++
    }
    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_activity_dashboard, fragment)
            commitNow()
        }
    private fun goHomeFragment(){
        val fragmentHome = HomeFragment()
        findViewById<ChipNavigationBar>(R.id.nav_view)?.setItemSelected(R.id.home)
        supportFragmentManager.beginTransaction().apply {
            add(R.id.nav_host_fragment_activity_dashboard,fragmentHome)
            commitNow()
        }
    }
}