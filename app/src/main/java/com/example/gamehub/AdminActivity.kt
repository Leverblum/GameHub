package com.example.gamehub

import android.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gamehub.admin.fragments.AdminGamesFragment
import com.example.gamehub.admin.fragments.AdminHomeFragment
import com.example.gamehub.admin.fragments.AdminSettingsFragment
import com.example.gamehub.admin.fragments.AdminUsersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class AdminActivity : AppCompatActivity() {
    var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        bottomNavigationView = findViewById<BottomNavigationView?>(R.id.bottom_navigation_admin)

        // Cargar fragment por defecto
        loadFragment(AdminHomeFragment())

        bottomNavigationView!!.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem? ->
            var selectedFragment: Fragment? = null
            when (item!!.getItemId()) {
                R.id.nav_admin_home -> selectedFragment = AdminHomeFragment()
                R.id.nav_admin_users -> selectedFragment = AdminUsersFragment()
                R.id.nav_admin_games -> selectedFragment = AdminGamesFragment()
                R.id.nav_admin_settings -> selectedFragment = AdminSettingsFragment()
            }
            loadFragment(selectedFragment)
        })
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.admin_fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }
}