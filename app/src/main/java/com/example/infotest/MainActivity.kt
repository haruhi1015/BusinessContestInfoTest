package com.example.infotest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : ComponentActivity() {

    private val validUsername = "info"
    private val validPassword = "tid"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.loginBtn)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username == validUsername && password == validPassword) {
                //ユーザーネームを保管
                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                prefs.edit().putString("USERNAME", username).apply()

                // Login successful, navigate to HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish() // Close LoginActivity
            } else {
                // Login failed, show a message to the user
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        val new : Button =findViewById(R.id.demoBtn)
        new.setOnClickListener {
            //ユーザーネームを保管
            val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
            prefs.edit().putString("USERNAME", "デモ").apply()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}