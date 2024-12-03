package com.example.cook

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.core.content.ContextCompat

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

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnReg = view.findViewById<Button>(R.id.btnRegister)
        val btnAuth = view.findViewById<Button>(R.id.btnLogin)
        val btnExit = view.findViewById<Button>(R.id.btnExit)
        val btnChangeTheme = view.findViewById<ImageView>(R.id.changeTheme)
        val profileName = view.findViewById<TextView>(R.id.account_name)
        val profileBack = view.findViewById<ConstraintLayout>(R.id.profileBack)

        val textProfile = view.findViewById<TextView>(R.id.textView)

        val settingsPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        var isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        textProfile.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isDarkMode) R.color.white else R.color.black
            )
        )
        profileName.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isDarkMode) R.color.white else R.color.black
            )
        )

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

            profileName.text = "You are not logged in yet"
            Toast.makeText(context, "You logged out from your account", Toast.LENGTH_SHORT).show()
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

            textProfile.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isDarkMode) R.color.white else R.color.black
                )
            )
            profileName.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isDarkMode) R.color.white else R.color.black
                )
            )

            profileBack?.setBackgroundResource(
                if (isDarkMode) R.drawable.dark_pattern_design else R.drawable.pattern_design
            )

            btnChangeTheme.setImageResource(
                if (isDarkMode) R.drawable.baseline_dark_mode_24 else R.drawable.baseline_sunny_24
            )
        }

        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "You are not logged in yet")
        if(userName == "You are not logged in yet"){
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


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}