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
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var adapter: ItemsAdapter
private lateinit var recycler: RecyclerView

private lateinit var itemsArrayList: ArrayList<Item>

class FavouritesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        val sharedPreferencesLang = requireContext().getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferencesLang.getString("selected_language", "ru")

        dbHelper = DbHelper(requireContext(), null)

        val warningText = view.findViewById<TextView>(R.id.warningFav)
        val warningNo = view.findViewById<TextView>(R.id.warningNo)

        val sharedPreferencesUser = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferencesUser.getString("userName", "Unknown")
        val userId = dbHelper.getUserId(userName!!)
        itemsArrayList = ArrayList(dbHelper.getFavoritesByUserAndLanguage(userId, language!!))
        if(userName == "Unknown"){
            warningText.visibility = View.VISIBLE
        }
        else if(itemsArrayList.isEmpty()){
            warningNo.visibility = View.VISIBLE
        }
        else{
            warningText.visibility = View.GONE
            warningNo.visibility = View.GONE
            val layoutManager = GridLayoutManager(context, 2)
            recycler = view.findViewById(R.id.favouritesList)
            recycler.layoutManager = layoutManager
            recycler.hasFixedSize()
            adapter = ItemsAdapter(itemsArrayList, isDarkMode)
            recycler.adapter = adapter
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
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        val settingsPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        val bgFavourites = view.findViewById<ScrollView>(R.id.fragment_container)
        val textFavourites = view.findViewById<TextView>(R.id.textView)
        val textWarningFav = view.findViewById<TextView>(R.id.warningFav)
        val textWarningNo = view.findViewById<TextView>(R.id.warningNo)

        bgFavourites.setBackgroundResource(
            if (isDarkMode) R.drawable.dark_pattern_design else R.drawable.pattern_design
        )
        applyTextColor(requireContext(), textFavourites, isDarkMode)
        applyTextColor(requireContext(), textWarningFav, isDarkMode)
        applyTextColor(requireContext(), textWarningNo, isDarkMode)

        return view
    }

    companion object {
        private const val REQUEST_CODE_ADD = 1
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun refreshItems() {
        if (!::adapter.isInitialized || !::recycler.isInitialized) {
            return
        }

        val sharedPreferencesUser = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferencesUser.getString("userName", "Unknown")

        val sharedPreferencesLang = requireContext().getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferencesLang.getString("selected_language", "ru")

        val userId = dbHelper.getUserId(userName!!)
        val updatedFavorites = dbHelper.getFavoritesByUserAndLanguage(userId, language!!)

        itemsArrayList.clear()
        itemsArrayList.addAll(updatedFavorites)
        adapter.notifyDataSetChanged()

        val warningText = view?.findViewById<TextView>(R.id.warningFav)
        val warningNo = view?.findViewById<TextView>(R.id.warningNo)
        if(userName == "Unknown"){
            warningText?.visibility = View.VISIBLE
        }
        else if(itemsArrayList.isEmpty()){
            warningNo?.visibility = View.VISIBLE
            recycler.visibility = View.GONE
        }
        else{
            warningText?.visibility = View.GONE
            warningNo?.visibility = View.GONE
            recycler.visibility = View.VISIBLE
        }

    }

    override fun onResume() {
        refreshItems()
        super.onResume()
    }
}