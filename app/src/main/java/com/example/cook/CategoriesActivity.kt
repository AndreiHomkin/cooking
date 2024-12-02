package com.example.cook

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriesActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemsAdapter
    private lateinit var dbHelper: DbHelper
    private lateinit var itemsArrayList: ArrayList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        val categoryName = intent.getStringExtra("catName")
        val subCategoryName = intent.getStringExtra("subcatName")
        val catNameTextView = findViewById<TextView>(R.id.catNameActivity)
        val buttonExit = findViewById<ImageView>(R.id.backBtn)
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
        adapter = ItemsAdapter(itemsArrayList)
        recyclerView.adapter = adapter
    }
}