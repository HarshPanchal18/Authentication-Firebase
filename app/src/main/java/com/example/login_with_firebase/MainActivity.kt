package com.example.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginbtn.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        registerbtn.setOnClickListener{
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}
