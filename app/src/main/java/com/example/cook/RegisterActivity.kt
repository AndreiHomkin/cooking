package com.example.cook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception

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
                Toast.makeText(this, "Not all fields are filled", Toast.LENGTH_LONG).show()
            else{

                val db = DbHelper(this, null)
                val isContains = email.lowercase().contains("@gmail.com".lowercase())
                if(db.isUserExist(login)){
                    Toast.makeText(this, "User $login already exist", Toast.LENGTH_LONG).show()
                }
                else if(db.isEmailExist(email)){
                    Toast.makeText(this, "Email $email is already used", Toast.LENGTH_LONG).show()
                }
                else if(!isContains){
                    Toast.makeText(this, "Email is incorrect", Toast.LENGTH_LONG).show()
                }
                else if(pass.length < 5){
                    Toast.makeText(this, "Too simple password", Toast.LENGTH_LONG).show()
                }
                else{
                    val user = User(login, email, pass)
                    db.addUser(user)
                    Toast.makeText(this, "User $login got added", Toast.LENGTH_LONG).show()

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

}