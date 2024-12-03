package com.example.cook

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
import androidx.core.content.ContextCompat
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
        dbHelper = DbHelper(requireContext(), null)

        val warningText = view.findViewById<TextView>(R.id.warningFav)
        val warningNo = view.findViewById<TextView>(R.id.warningNo)

        val sharedPreferencesUser = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferencesUser.getString("userName", "Unknown")
        val userId = dbHelper.getUserId(userName!!)
        itemsArrayList = ArrayList(dbHelper.getFavoritesByUser(userId))
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
            adapter = ItemsAdapter(itemsArrayList)
            recycler.adapter = adapter
        }
    }

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
        textFavourites.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isDarkMode) R.color.white else R.color.black
            )
        )
        textWarningFav.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isDarkMode) R.color.white else R.color.black
            )
        )
        textWarningNo.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isDarkMode) R.color.white else R.color.black
            )
        )

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
        private const val REQUEST_CODE_ADD = 1
    }
    private fun refreshItems() {
        val sharedPreferencesUser = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferencesUser.getString("userName", "Unknown")
        val userId = dbHelper.getUserId(userName!!)
        itemsArrayList.clear()
        itemsArrayList.addAll(dbHelper.getFoodsByUser(userId))
        adapter.notifyDataSetChanged()
    }
}