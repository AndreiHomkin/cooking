package com.example.cook

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "everything", factory, 6) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "login TEXT, " +
                "email TEXT, " +
                "pass TEXT)"
        val queryFood = "CREATE TABLE food(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "category TEXT, " +
                "subcategory TEXT, " +
                "description TEXT, " +
                "ingredients TEXT, " +
                "image TEXT, " +
                "user_id INTEGER, " +
                "language TEXT, " +
                "visibility TEXT DEFAULT 'public', " +
                "FOREIGN KEY (user_id) REFERENCES users(id))"
        val queryFavourites = "CREATE TABLE favorites(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "food_id INTEGER, " +
                "FOREIGN KEY (user_id) REFERENCES users(id), " +
                "FOREIGN KEY (food_id) REFERENCES food(id))"
        db!!.execSQL(query)
        db.execSQL(queryFood)
        db.execSQL(queryFavourites)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS food")
        db.execSQL("DROP TABLE IF EXISTS favorites")
        onCreate(db)
    }

    fun addFood(item: Item, userId: Int, lang: String, visibility: String = "public"){
        val values = ContentValues()
        values.put("name", item.name)
        values.put("category", item.category)
        values.put("subcategory", item.subcategory)
        values.put("description", item.desc)
        values.put("ingredients", item.ingredients)
        values.put("image", item.image)
        values.put("user_id", userId)
        values.put("language", lang)
        values.put("visibility", visibility)

        val db = this.writableDatabase
        db.insert("food", null, values)

        db.close()
    }
    fun getUserId(login: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE login = ?", arrayOf(login))
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        return userId
    }

    fun addUser(user: User){
        val values = ContentValues()
        values.put("login", user.login)
        values.put("email", user.email)
        values.put("pass", user.pass)

        val db = this.writableDatabase
        db.insert("users", null, values)

        db.close()
    }

    @SuppressLint("Recycle")
    fun getUser(login: String, pass: String): Boolean{
        val hashedPassword = hashPassword(pass)
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM users WHERE login = '$login' AND pass = '$hashedPassword'", null)
        return result.moveToFirst()
    }

    fun isUserExist(login: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query("users", arrayOf("login"), "login=?", arrayOf(login), null, null, null)
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }
    fun getAllFoods(): List<Item> {
        val foodList = mutableListOf<Item>()

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE visibility = 'public'", null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val subcategory = cursor.getString(cursor.getColumnIndexOrThrow("subcategory"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"))

                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))

                val foodItem = Item(name, category,subcategory, description, ingredients, image)
                foodList.add(foodItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return foodList
    }
    fun isEmailExist(email: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
    fun deleteFood(item: Item) {
        val db = this.writableDatabase
        db.delete("food", "name = ?", arrayOf(item.name)) // Delete food by id
        db.close()
    }
    fun getFoodIdByName(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM food WHERE name = ?", arrayOf(name))
        var foodId = -1

        if (cursor.moveToFirst()) {
            foodId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }
        cursor.close()
        return foodId
    }
    fun getFoodsByCategory(category: String): List<Item> {
        val foodList = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE category = ? AND visibility = 'public'", arrayOf(category))

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val subcategory = cursor.getString(cursor.getColumnIndexOrThrow("subcategory"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))

                val foodItem = Item(name, category,subcategory, description, ingredients, image)
                foodList.add(foodItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return foodList
    }
    fun getFoodsBySubCategory(subcategory: String): List<Item> {
        val foodList = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE subcategory = ? AND visibility = 'public'", arrayOf(subcategory))

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))

                val foodItem = Item(name, category,subcategory, description, ingredients, image)
                foodList.add(foodItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return foodList
    }
    fun getFoodByName(name: String): Item? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE name = ?", arrayOf(name))

        var foodItem: Item? = null

        if (cursor.moveToFirst()) {
            val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            val subcategory = cursor.getString(cursor.getColumnIndexOrThrow("subcategory"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"))
            val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))

            foodItem = Item(name, category, subcategory, description, ingredients, image)
        }

        cursor.close()
        return foodItem
    }
    fun addFavorite(userId: Int, foodId: Int) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("user_id", userId)
        values.put("food_id", foodId)

        db.insert("favorites", null, values)
        db.close()
    }
    fun removeFavorite(userId: Int, foodId: Int) {
        val db = this.writableDatabase
        db.delete("favorites", "user_id = ? AND food_id = ?", arrayOf(userId.toString(), foodId.toString()))
        db.close()
    }
    fun isFavorite(userId: Int, foodId: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM favorites WHERE user_id = ? AND food_id = ?",
            arrayOf(userId.toString(), foodId.toString())
        )
        val isFavorite = cursor.moveToFirst()
        cursor.close()
        return isFavorite
    }
    fun isRecipeExist(recipe: Item): Boolean {
        val db = this.readableDatabase

        val query = "SELECT * FROM food WHERE LOWER(name) = LOWER(?) or image = ?"
        val cursor = db.rawQuery(query, arrayOf(recipe.name))

        val exists = cursor.count > 0
        cursor.close()

        return exists
    }
    fun getFoodByNameAndLanguage(name: String, lang: String): Item? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE name = ? AND language = ?", arrayOf(name, lang))

        var foodItem: Item? = null

        if (cursor.moveToFirst()) {
            val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
            val subcategory = cursor.getString(cursor.getColumnIndexOrThrow("subcategory"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"))
            val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))

            foodItem = Item(name, category, subcategory, description, ingredients, image)
        }

        cursor.close()
        return foodItem
    }
    fun getAllFoodsByLanguage(lang: String): List<Item> {
        val foodList = mutableListOf<Item>()

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE language = ? AND visibility = 'public'", arrayOf(lang))

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val subcategory = cursor.getString(cursor.getColumnIndexOrThrow("subcategory"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"))

                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))

                val foodItem = Item(name, category,subcategory, description, ingredients, image)
                foodList.add(foodItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return foodList
    }
    fun getFoodsByUserAndLanguage(userId: Int, lang: String): List<Item> {
        val foodList = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM food WHERE user_id = ? AND language = ?", arrayOf(userId.toString(), lang))

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val subcategory = cursor.getString(cursor.getColumnIndexOrThrow("subcategory"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))

                val foodItem = Item(name, category,subcategory, description, ingredients, image)
                foodList.add(foodItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return foodList
    }
    fun getFavoritesByUserAndLanguage(userId: Int, lang: String): List<Item> {
        val foodList = mutableListOf<Item>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT food.* FROM food 
        INNER JOIN favorites ON food.id = favorites.food_id 
        WHERE favorites.user_id = ? AND food.language = ?
        """,
            arrayOf(userId.toString(), lang)
        )

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val category = cursor.getString(cursor.getColumnIndexOrThrow("category"))
                val subcategory = cursor.getString(cursor.getColumnIndexOrThrow("subcategory"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("image"))

                val foodItem = Item(name, category, subcategory, description, ingredients, image)
                foodList.add(foodItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return foodList
    }
}