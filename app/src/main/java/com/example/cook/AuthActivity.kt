package com.example.cook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPass: EditText = findViewById(R.id.user_pass_auth)
        val button: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.link_to_reg)


        linkToReg.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if(login == "" || pass == "")
                Toast.makeText(this, "Not all fields are filled", Toast.LENGTH_LONG).show()
            else{
                val db = DbHelper(this, null)
                val isAuth = db.getUser(login, pass)

                if(isAuth){
                    Toast.makeText(this, "User $login logged in", Toast.LENGTH_LONG).show()
                    userLogin.text.clear()
                    userPass.text.clear()

                    val userId = db.getUserId(login)
                    val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("userId", userId)
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("userName", login)
                    editor.apply()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userName", login)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "Data is incorrect", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}