package com.example.cook

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.io.File


class ViewItemActivity : AppCompatActivity() {
    private lateinit var dbHelper: DbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)
        val itemName = intent.getStringExtra("itemTitle")
        val wasCreatedByYou = intent.getBooleanExtra("createdByYou", false)
        val btnAddFavourite = findViewById<ImageView>(R.id.addFavourite)

        if (itemName != null) {
            dbHelper = DbHelper(this, null)
            val sharedPreferencesUser = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val userName = sharedPreferencesUser.getString("userName", "Unknown")
            val userId = dbHelper.getUserId(userName!!)
            if(userName == "Unknown" || wasCreatedByYou){
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
}