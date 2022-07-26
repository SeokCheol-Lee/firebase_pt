package com.example.firebase_pt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.firebase_pt.Fragment.ChatFragment
import com.example.firebase_pt.Fragment.HomeFragment
import com.example.firebase_pt.Fragment.ProfiileFragment
import com.example.firebase_pt.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

private lateinit var homeFragment: HomeFragment
private lateinit var chatFragment: ChatFragment
private lateinit var profileFragment: ProfiileFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.bottomNav.setOnNavigationItemSelectedListener(BottomNavItemSelectedListener)
        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame, homeFragment).commit()

    }
    private val BottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{
        when(it.itemId){
            R.id.menu_home -> {
                homeFragment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragments_frame, homeFragment).commit()
            }
            R.id.menu_chat -> {
                chatFragment = ChatFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragments_frame, chatFragment).commit()
            }
            R.id.menu_profile -> {
                profileFragment = ProfiileFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragments_frame, profileFragment).commit()
            }
        }
        true
    }
}