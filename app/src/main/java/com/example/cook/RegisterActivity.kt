package com.example.cook

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest
import java.util.Locale

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userEmail: EditText = findViewById(R.id.user_email)
        val userPass: EditText = findViewById(R.id.user_pass)
        val button: Button = findViewById(R.id.button_reg)
        val linkToAuth: TextView = findViewById(R.id.link_to_auth)

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()
            val email = userEmail.text.toString().trim()

            if(login == "" || email == "" || pass == "")
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_LONG).show()
            else{

                val db = DbHelper(this, null)
                val isContains = email.lowercase().contains("@gmail.com".lowercase())
                if(db.isUserExist(login)){
                    Toast.makeText(this,
                        getString(R.string.user_already_exist, login), Toast.LENGTH_LONG).show()
                }
                else if(db.isEmailExist(email)){
                    Toast.makeText(this,
                        getString(R.string.email_is_already_used, email), Toast.LENGTH_LONG).show()
                }
                else if(!isContains){
                    Toast.makeText(this, getString(R.string.email_is_incorrect), Toast.LENGTH_LONG).show()
                }
                else if(pass.length < 5){
                    Toast.makeText(this, getString(R.string.too_simple_password), Toast.LENGTH_LONG).show()
                }
                else{
                    val hashedPassword = hashPassword(pass)
                    val user = User(login, email, hashedPassword)
                    db.addUser(user)
                    Toast.makeText(this,
                        getString(R.string.user_got_added, login), Toast.LENGTH_LONG).show()

                    userLogin.text.clear()
                    userEmail.text.clear()
                    userPass.text.clear()
                }
            }
        }
        linkToAuth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
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