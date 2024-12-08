package com.example.cook

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var bottom_navigation: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("selected_language", "en")
        val locale = Locale(language!!)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        if (!isNetworkAvailable()) {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.error_title))
                setMessage(getString(R.string.error_message))
                setPositiveButton(getString(R.string.yes)) { dialog: DialogInterface, _: Int ->
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
                currentFragment?.let { makeCurrentFragment(it) }
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

    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = newBase?.getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferences?.getString("selected_language", "en") ?: "en"

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(newBase?.resources?.configuration).apply {
            setLocale(locale)
        }
        super.attachBaseContext(newBase?.createConfigurationContext(config))
    }
    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.exit_app_title)
            setMessage(R.string.exit_app_message)
            setPositiveButton(R.string.yes) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                finish()
            }
            setNegativeButton(R.string.no) { dialog: DialogInterface, _: Int ->
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
    private fun makeCurrentFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        if (currentFragment != fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()
        }
    }
    fun updateLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        recreate() // Перезапуск активности для применения изменений
    }
}