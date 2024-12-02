package com.example.cook

class Category(val name: String, val image: Int) {
    override fun toString(): String {
        return "$name|$image"
    }
}