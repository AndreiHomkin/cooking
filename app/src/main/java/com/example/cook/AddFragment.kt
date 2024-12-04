@file:Suppress("DEPRECATION")

package com.example.cook

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbHelper: DbHelper
    private lateinit var itemsArrayList: ArrayList<Item>
    private lateinit var adapter: ItemsAdapterPersonal
    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD && resultCode == AppCompatActivity.RESULT_OK) {
            refreshItems()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        val settingsPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        val btnAddItem = view.findViewById<ImageView>(R.id.addReceipt)
        val bgAdd = view.findViewById<ScrollView>(R.id.addBack)
        val textAdd = view.findViewById<TextView>(R.id.catNameActivity)
        val textWarning = view.findViewById<TextView>(R.id.warning)

        bgAdd.setBackgroundResource(
            if (isDarkMode) R.drawable.dark_pattern_design else R.drawable.pattern_design
        )
        textAdd.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isDarkMode) R.color.white else R.color.black
            )
        )
        textWarning.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isDarkMode) R.color.white else R.color.black
            )
        )

        btnAddItem.setOnClickListener {
            val intent = Intent(requireContext(), AddActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DbHelper(requireContext(), null)
        val sharedPreferencesUser = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferencesUser.getString("userName", "Unknown")
        val userId = dbHelper.getUserId(userName!!)
        itemsArrayList = ArrayList(dbHelper.getFoodsByUser(userId))
        val btnAddItem = view.findViewById<ImageView>(R.id.addReceipt)
        val textWarning = view.findViewById<TextView>(R.id.warning)
        if(userName == "Unknown"){
            btnAddItem.visibility = View.INVISIBLE
            textWarning.visibility = View.VISIBLE
        }
        else{
            btnAddItem.visibility = View.VISIBLE
            textWarning.visibility = View.GONE
        }

        val layoutManager = GridLayoutManager(context, 2)
        recycler = view.findViewById(R.id.foodList)
        recycler.layoutManager = layoutManager
        recycler.hasFixedSize()
        adapter = ItemsAdapterPersonal(itemsArrayList)
        recycler.adapter = adapter
    }

    companion object {
        private const val REQUEST_CODE_ADD = 1
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshItems() {
        val sharedPreferencesUser = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferencesUser.getString("userName", "Unknown")
        val userId = dbHelper.getUserId(userName!!)
        itemsArrayList.clear()
        itemsArrayList.addAll(dbHelper.getFoodsByUser(userId))
        adapter.notifyDataSetChanged()
    }
}