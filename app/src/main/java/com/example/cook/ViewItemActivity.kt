package com.example.cook

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.Locale


class ViewItemActivity : AppCompatActivity() {
    private lateinit var dbHelper: DbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        val itemName = intent.getStringExtra("itemTitle")
        val btnAddFavourite = findViewById<ImageView>(R.id.addFavourite)

        val settingsPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        if (itemName != null) {
            dbHelper = DbHelper(this, null)
            val sharedPreferencesUser = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userName = sharedPreferencesUser.getString("userName", "Unknown")
            val userId = dbHelper.getUserId(userName!!)
            if(userName == "Unknown"){
                btnAddFavourite.visibility = View.GONE
            }
            else{
                btnAddFavourite.visibility = View.VISIBLE
            }

            val item = dbHelper.getFoodByName(itemName)

            if (item != null) {
                val itemId = dbHelper.getFoodIdByName(item.name)
                item.isFavorite = dbHelper.isFavorite(userId, itemId)

                updateFavoriteButton(btnAddFavourite, item.isFavorite)

                val itemImage = findViewById<ImageView>(R.id.item_show_image)
                val itemNameText = findViewById<TextView>(R.id.item_show_title)
                val itemCategory = findViewById<TextView>(R.id.item_show_cat)
                val itemSubCategory = findViewById<TextView>(R.id.item_show_subcat)
                val itemDescr = findViewById<TextView>(R.id.item_show_desc)
                val itemIngr = findViewById<TextView>(R.id.item_show_ingredients)

                val whenToEat = findViewById<TextView>(R.id.textView3)
                val categoryText = findViewById<TextView>(R.id.textView4)
                val ingrText = findViewById<TextView>(R.id.textView5)
                val descrText = findViewById<TextView>(R.id.textView6)

                applyTextColor(this, itemNameText, isDarkMode)
                applyTextColor(this, itemCategory, isDarkMode)
                applyTextColor(this, itemSubCategory, isDarkMode)
                applyTextColor(this, itemDescr, isDarkMode)
                applyTextColor(this, itemIngr, isDarkMode)
                applyTextColor(this, whenToEat, isDarkMode)
                applyTextColor(this, categoryText, isDarkMode)
                applyTextColor(this, ingrText, isDarkMode)
                applyTextColor(this, descrText, isDarkMode)

                itemNameText.text = item.name
                itemCategory.text = item.category
                itemSubCategory.text = item.subcategory
                itemDescr.text = item.desc
                itemIngr.text = item.ingredients


                if (!item.image.isNullOrEmpty()) {
                    val imageUri = Uri.parse(item.image)
                    try {
                        contentResolver.takePersistableUriPermission(
                            imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (e: SecurityException) {
                        Log.e("ViewItemActivity", "Failed to take persistable URI permission", e)
                    }
                    Glide.with(this)
                        .load(imageUri)
                        .apply(RequestOptions().error(R.drawable.redx))
                        .into(itemImage)
                }
                else{
                    itemImage.setImageResource(R.drawable.dinner)
                }
                btnAddFavourite.setOnClickListener {
                    if (item.isFavorite) {
                        dbHelper.removeFavorite(userId, itemId)
                        item.isFavorite = false
                    } else {
                        dbHelper.addFavorite(userId, itemId)
                        item.isFavorite = true
                    }
                    updateFavoriteButton(btnAddFavourite, item.isFavorite)
                }
            }
        }
    }
    private fun updateFavoriteButton(button: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            button.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            button.setImageResource(R.drawable.baseline_favorite_border_24)
        }
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