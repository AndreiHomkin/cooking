package com.example.cook

class Item(val name: String, val category: String,val subcategory: String, val desc: String, val ingredients: String, val image: String?, var isFavorite: Boolean = false) {
    override fun toString(): String {
        return "$name|$category|$subcategory|$desc|$ingredients|$image|$isFavorite"
    }
}