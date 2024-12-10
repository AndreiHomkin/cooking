package com.example.cook

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId", "ResourceType", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnReg = view.findViewById<Button>(R.id.btnRegister)
        val btnAuth = view.findViewById<Button>(R.id.btnLogin)
        val btnExit = view.findViewById<Button>(R.id.btnExit)
        val btnChangeTheme = view.findViewById<ImageView>(R.id.changeTheme)
        val btnChangeLanguage = view.findViewById<ImageView>(R.id.changeLanguage)
        val profileName = view.findViewById<TextView>(R.id.account_name)
        val profileBack = view.findViewById<ConstraintLayout>(R.id.profileBack)

        val textProfile = view.findViewById<TextView>(R.id.textView)

        val settingsPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        var isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        val sharedPreferencesLang = requireContext().getSharedPreferences("language", Context.MODE_PRIVATE)
        var language = sharedPreferencesLang.getString("selected_language", "ru")

        applyTextColor(requireContext(), textProfile, isDarkMode)
        applyTextColor(requireContext(), profileName, isDarkMode)

        profileBack.setBackgroundResource(
            if (isDarkMode) R.drawable.dark_pattern_design else R.drawable.pattern_design
        )

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        btnChangeTheme.setImageResource(
            if (isDarkMode) R.drawable.baseline_dark_mode_24 else R.drawable.baseline_sunny_24
        )
        btnReg.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }
        btnAuth.setOnClickListener {
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
        }
        btnExit.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            profileName.text = getString(R.string.you_are_not_logged_in_yet)
            Toast.makeText(context,
                getString(R.string.you_logged_out_from_your_account), Toast.LENGTH_SHORT).show()
            btnExit.visibility = View.GONE
            btnAuth.visibility = View.VISIBLE
            btnReg.visibility = View.VISIBLE
        }
        btnChangeTheme.setOnClickListener {
            isDarkMode = !isDarkMode
            val mode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)

            val editor = settingsPreferences.edit()
            editor.putBoolean("isDarkMode", isDarkMode)
            editor.apply()

            applyTextColor(requireContext(), textProfile, isDarkMode)
            applyTextColor(requireContext(), profileName, isDarkMode)

            profileBack?.setBackgroundResource(
                if (isDarkMode) R.drawable.dark_pattern_design else R.drawable.pattern_design
            )

            btnChangeTheme.setImageResource(
                if (isDarkMode) R.drawable.baseline_dark_mode_24 else R.drawable.baseline_sunny_24
            )
        }
        btnChangeLanguage.setImageResource(
            if (language == "ru") R.drawable.russia else R.drawable.us
        )

        btnChangeLanguage.setOnClickListener {
            val editor = sharedPreferencesLang.edit()

            language = if (language == "ru") "en" else "ru"
            editor.putString("selected_language", language)
            editor.apply()

            btnChangeLanguage.setImageResource(
                if (language == "ru") R.drawable.russia else R.drawable.us
            )
            val locale = Locale(language!!)
            Locale.setDefault(locale)
            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)

            textProfile.text = getString(R.string.profile)
            btnReg.text = getString(R.string.create_an_account)
            btnAuth.text = getString(R.string.login_to_your_account)
            btnExit.text = getString(R.string.logout_from_account)

            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            val menu = bottomNavigationView.menu

            menu.findItem(R.id.home).title = getString(R.string.home_menu)
            menu.findItem(R.id.add).title = getString(R.string.add_menu)
            menu.findItem(R.id.favourites).title = getString(R.string.favourites_menu)
            menu.findItem(R.id.profile).title = getString(R.string.profile_menu)
            menu.findItem(R.id.search).title = getString(R.string.settings_menu)

            profileName.text = getString(R.string.you_are_not_logged_in_yet)

            val intent = requireActivity().intent
            requireActivity().finish()
            requireActivity().startActivity(intent)
        }

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", getString(R.string.you_are_not_logged_in_yet))

        if(userName == getString(R.string.you_are_not_logged_in_yet)){
            btnExit.visibility = View.GONE
            btnAuth.visibility = View.VISIBLE
            btnReg.visibility = View.VISIBLE
        }
        else{
            btnExit.visibility = View.VISIBLE
            btnAuth.visibility = View.INVISIBLE
            btnReg.visibility = View.INVISIBLE
        }

        profileName.text = userName

        return view
    }


    companion object
}