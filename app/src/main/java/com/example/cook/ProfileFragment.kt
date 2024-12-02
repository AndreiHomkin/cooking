package com.example.cook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnReg = view.findViewById<Button>(R.id.btnRegister)
        val btnAuth = view.findViewById<Button>(R.id.btnLogin)
        val btnExit = view.findViewById<Button>(R.id.btnExit)
        val profileName = view.findViewById<TextView>(R.id.account_name)
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