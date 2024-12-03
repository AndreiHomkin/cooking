package com.example.cook

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottom_navigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isNetworkAvailable()) {
            AlertDialog.Builder(this).apply {
                setTitle("Error")
                setMessage("There is no internet connection. The application cannot be used without an internet connection.")
                setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    finish()
                }
                setCancelable(false)
                create()
                show()
            }
            return
        }
        else{
            bottom_navigation = findViewById(R.id.bottomNavigationView)

            val homeFragment = HomeFragment()
            val favouritesFragment = FavouritesFragment()
            val profileFragment = ProfileFragment()
            val searchFragment = SearchFragment()
            val addFragment = AddFragment()

            if (savedInstanceState == null) {
                makeCurrentFragment(homeFragment)
            } else {
                val currentFragment = supportFragmentManager.findFragmentByTag("CURRENT_FRAGMENT")
                currentFragment?.let { makeCurrentFragment(it as Fragment) }
            }

            bottom_navigation.setOnItemSelectedListener{
                when(it.itemId){
                    R.id.home -> makeCurrentFragment(homeFragment)
                    R.id.add -> makeCurrentFragment(addFragment)
                    R.id.favourites -> makeCurrentFragment(favouritesFragment)
                    R.id.profile -> makeCurrentFragment(profileFragment)
                    R.id.search -> makeCurrentFragment(searchFragment)
                }
                true
            }

            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
            if (isLoggedIn) {
                val userName = sharedPreferences.getString("userName", "Guest")
            }

            val sharedPreferences123 = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            val isAppOpen = sharedPreferences123.getBoolean("isAppOpen", false)
            if (!isAppOpen) {
                sharedPreferences123.edit().clear().apply()
            }
            sharedPreferences123.edit().putBoolean("isAppOpen", true).apply()

            val sharedPreferencesSettings = getSharedPreferences("Settings", Context.MODE_PRIVATE)
            val isDarkMode = sharedPreferencesSettings.getBoolean("isDarkMode", false)
            if(isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Exit from the app")
            setMessage("Are you sure you want to quit?")
            setPositiveButton("Yes") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                finish()
            }
            setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    override fun onStop() {
        super.onStop()
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isAppOpen", false).apply()
    }
    override fun onStart() {
        super.onStart()

        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isAppOpen", true).apply()
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.activeNetwork ?: return false
        }
        else {
            TODO("VERSION.SDK_INT < M")
        }
        val capabilities = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.frame_layout, fragment)
            commit()
        }
}