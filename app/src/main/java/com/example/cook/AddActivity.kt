@file:Suppress("DEPRECATION")

package com.example.cook

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.util.Locale


class AddActivity : AppCompatActivity() {
    private lateinit var categories: Array<String>
    private lateinit var subcategories: Array<String>
    private val REQUEST_CODE = 1
    private var item: Item? = null
    private var selectedCategory: String? = null
    private var selectedSubCategory: String? = null
    private lateinit var addImage: ImageView
    private lateinit var nameReceipt: EditText
    private lateinit var ingReceipt: EditText
    private lateinit var descReceipt: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        categories = arrayOf(
            getString(R.string.breakfast),
            getString(R.string.lunch),
            getString(R.string.dinner),
            getString(R.string.supper)
        )
        subcategories = arrayOf(
            getString(R.string.appetizers),
            getString(R.string.salads),
            getString(R.string.main_dishes),
            getString(R.string.soups),
            getString(R.string.desserts),
            getString(R.string.drinks)
        )

        val settingsPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDarkMode = settingsPreferences.getBoolean("isDarkMode", false)

        val textAddReceipt = findViewById<TextView>(R.id.catNameActivity)
        val bgAddActivity = findViewById<ConstraintLayout>(R.id.addActivity)
        val receiptNameText = findViewById<TextView>(R.id.receiptNameText)
        val receiptCatText = findViewById<TextView>(R.id.receiptCatText)
        val receiptSubcatText = findViewById<TextView>(R.id.subCat)
        val receiptIngrText = findViewById<TextView>(R.id.receiptIngrText)
        val receiptStepsText = findViewById<TextView>(R.id.textView6)
        val receiptImageText = findViewById<TextView>(R.id.textView5)
        val receiptName = findViewById<EditText>(R.id.receiptName)
        val receiptIngr = findViewById<EditText>(R.id.receiptIngr)
        val receiptDecr = findViewById<EditText>(R.id.receiptDesc)

        bgAddActivity.setBackgroundResource(
            if (isDarkMode) R.drawable.dark_pattern_design else R.drawable.pattern_design
        )

        applyTextColor(this, receiptName, isDarkMode)
        applyTextColor(this,receiptIngr, isDarkMode)
        applyTextColor(this,receiptDecr, isDarkMode)
        applyTextColor(this,textAddReceipt, isDarkMode)
        applyTextColor(this,receiptNameText, isDarkMode)
        applyTextColor(this,receiptCatText, isDarkMode)
        applyTextColor(this,receiptSubcatText, isDarkMode)
        applyTextColor(this,receiptIngrText, isDarkMode)
        applyTextColor(this,receiptStepsText, isDarkMode)
        applyTextColor(this,receiptImageText, isDarkMode)

        val btnExit = findViewById<ImageView>(R.id.backBtn)
        btnExit.setImageResource(
            if (isDarkMode) R.drawable.baseline_arrow_back_24_white else R.drawable.baseline_arrow_back_24
        )
        btnExit.setOnClickListener {
            onBackPressed()
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val spinnerSub = findViewById<Spinner>(R.id.spinnerSub)
        addImage = findViewById(R.id.receiptImg)
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories)
        val adapterSub: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcategories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterSub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinnerSub.adapter = adapterSub

        requestPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE)

        addImage.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivityForResult(intent, REQUEST_CODE)
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                selectedCategory = parentView.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {

            }
        }
        spinnerSub.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
                selectedSubCategory = p0.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val buttonCreate: Button = findViewById(R.id.createItem)
        buttonCreate.setOnClickListener {
            nameReceipt = findViewById(R.id.receiptName)
            ingReceipt = findViewById(R.id.receiptIngr)
            descReceipt = findViewById(R.id.receiptDesc)

            if(nameReceipt.text.isNullOrEmpty() || ingReceipt.text.isNullOrEmpty() || descReceipt.text.isNullOrEmpty()){
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_LONG).show()
            }
            else{
                val sharedPreferences =
                    getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val backgroundUri = sharedPreferences.getString("backgroundUri", null)
                if (!backgroundUri.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(Uri.parse(backgroundUri))
                        .into(addImage)
                    saveItemToDatabase(
                        nameReceipt.text.toString(),
                        descReceipt.text.toString(),
                        ingReceipt.text.toString(),
                        backgroundUri
                    )
                }
                else{
                    addImage.setImageResource(R.drawable.dinner)
                    saveItemToDatabase(
                        nameReceipt.text.toString(),
                        descReceipt.text.toString(),
                        ingReceipt.text.toString(),
                        null
                    )
                }
                val editor = sharedPreferences.edit()
                editor.putString("backgroundUri", null)
                editor.apply()
            }
        }
    }





    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val uri: Uri? = data.data
            if (uri != null) {
                val mimeType = contentResolver.getType(uri)
                if (mimeType != null && mimeType.startsWith("image/")) {
                    Glide.with(this)
                        .load(uri)
                        .into(addImage)

                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("backgroundUri", uri.toString())
                    editor.apply()
                } else {
                    Toast.makeText(this, getString(R.string.select_image), Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }
    private fun saveItemToDatabase(name: String, description: String, ingredients: String, imageUri: String?) {
        val dbHelper = DbHelper(this, null)

        val sharedPreferencesUser = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferencesUser.getString("userName", "Unknown")

        val sharedPreferencesLang = getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferencesLang.getString("selected_language", "en")

        val userId = dbHelper.getUserId(userName!!)

        item = Item(name, selectedCategory!!, selectedSubCategory!!, description, ingredients, imageUri)

        if(dbHelper.isRecipeExist(item!!)){
            Toast.makeText(this, getString(R.string.receipt_already_exist), Toast.LENGTH_LONG).show()
            return
        }
        else{
            dbHelper.addFood(item!!, userId, language!!)
            Toast.makeText(this, getString(R.string.receipt_created_successfully), Toast.LENGTH_LONG).show()
            clearFields()
        }
    }
    private fun requestPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        }
    }
    private fun clearFields() {
        nameReceipt.text.clear()
        ingReceipt.text.clear()
        descReceipt.text.clear()
    }
    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = newBase?.getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferences?.getString("selected_language", "en") ?: "en"

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(newBase?.resources?.configuration).apply {
            setLocale(locale)
        }
        super.attachBaseContext(newBase?.createConfigurationContext(config))
    }

}