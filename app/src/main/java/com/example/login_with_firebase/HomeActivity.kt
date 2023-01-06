package com.example.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth=FirebaseAuth.getInstance()

        logoutbtn.setOnClickListener {
            auth.signOut()
            Intent(this,LoginActivity::class.java).also {
                it.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                Toast.makeText(this,"Logged out Successfully", Toast.LENGTH_SHORT).show()
            }
        }

        databtn.setOnClickListener {
            startActivity(Intent(this,DataPage::class.java))
        }
    }
}
