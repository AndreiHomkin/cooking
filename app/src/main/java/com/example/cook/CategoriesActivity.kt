@file:Suppress("DEPRECATION")

package com.example.cook

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class CategoriesActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemsAdapter
    private lateinit var dbHelper: DbHelper
    private lateinit var itemsArrayList: ArrayList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val settingsPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        val categoryName = intent.getStringExtra("catName")
        val subCategoryName = intent.getStringExtra("subcatName")
        val catNameTextView = findViewById<TextView>(R.id.catNameActivity)
        val buttonExit = findViewById<ImageView>(R.id.backBtn)

        val bgCategory = findViewById<ScrollView>(R.id.bgCategory)

        applyTextColor(this, catNameTextView, isDarkMode)

        bgCategory.setBackgroundResource(
            if (isDarkMode) R.drawable.dark_pattern_design else R.drawable.pattern_design
        )

        buttonExit.setImageResource(
            if (isDarkMode) R.drawable.baseline_arrow_back_24_white else R.drawable.baseline_arrow_back_24
        )

        if(categoryName.isNullOrEmpty()){
            catNameTextView.text = subCategoryName
        }
        else if(subCategoryName.isNullOrEmpty()){
            catNameTextView.text = categoryName
        }
        buttonExit.setOnClickListener {
            onBackPressed()
        }
        dbHelper = DbHelper(this, null)
        recyclerView = findViewById(R.id.foodList)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        itemsArrayList = ArrayList()

        loadFoodByCategory(categoryName, subCategoryName)
    }
    private fun loadFoodByCategory(category: String?, subCategory: String?) {
        val settingsPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)
        val foodList: List<Item> = when {
            !category.isNullOrEmpty() -> {
                dbHelper.getFoodsByCategory(category)
            }
            !subCategory.isNullOrEmpty() -> {
                dbHelper.getFoodsBySubCategory(subCategory)
            }
            else -> {
                dbHelper.getAllFoods()
            }
        }
        itemsArrayList.addAll(foodList)
        adapter = ItemsAdapter(itemsArrayList, isDarkMode)
        recyclerView.adapter = adapter
    }
    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = newBase?.getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferences?.getString("selected_language", "ru") ?: "ru"

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(newBase?.resources?.configuration).apply {
            setLocale(locale)
        }
        super.attachBaseContext(newBase?.createConfigurationContext(config))
    }
}