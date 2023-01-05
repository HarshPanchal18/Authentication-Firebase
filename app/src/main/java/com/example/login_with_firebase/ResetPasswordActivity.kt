package com.example.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.activity_reset_password.et_mail

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        auth=FirebaseAuth.getInstance()

        val emailStream= RxTextView.textChanges(et_mail)
            .skipInitialValue()
            .map { mail -> !Patterns.EMAIL_ADDRESS.matcher(mail).matches() }
        emailStream.subscribe{ showEmailValidAlert(it) }

        reset_pw_btn.setOnClickListener {
            val mail=et_mail.text.toString().trim()
            auth.sendPasswordResetEmail(mail)
                .addOnCompleteListener(this){ reset ->
                    if(reset.isSuccessful){
                        Intent(this,LoginActivity::class.java).also {
                            it.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                            Toast.makeText(this,"Check email for password reset", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this,reset.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        back_login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun showEmailValidAlert(isNotValid: Boolean) {
        if(isNotValid){
            et_mail.error="Email is not valid"
            reset_pw_btn.isEnabled=false
            reset_pw_btn.backgroundTintList= ContextCompat.getColorStateList(this,android.R.color.darker_gray)
        } else {
            et_mail.error=null
            reset_pw_btn.isEnabled=true
            reset_pw_btn.backgroundTintList=ContextCompat.getColorStateList(this,R.color.primary_color)
        }
    }
}
