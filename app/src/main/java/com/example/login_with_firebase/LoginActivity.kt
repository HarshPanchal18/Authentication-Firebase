package com.example.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("CheckResult")
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Authentication
        auth=FirebaseAuth.getInstance()

        val userNameStream = RxTextView.textChanges(et_mail)
            .skipInitialValue()
            .map { username -> username.isEmpty() }
        userNameStream.subscribe { showTextMinimalAlert(it,"Email/Username") }

        val passwordStream = RxTextView.textChanges(et_password)
            .skipInitialValue()
            .map { password -> password.isEmpty() }
        passwordStream.subscribe { showTextMinimalAlert(it,"Password") }


        // Button Enable/Disable
        val invalidFieldsStream :Observable<Boolean> = Observable.combineLatest(userNameStream,passwordStream,
        ) {
                usernameInvalid: Boolean, passwordInvalid: Boolean
            ->
            !usernameInvalid && !passwordInvalid
        }

        invalidFieldsStream.subscribe { isValid:Boolean ->
            //if(userNameStream != null && passwordStream!=null){
            //if(!TextUtils.isEmpty(et_mail.text) && !TextUtils.isEmpty(et_password.text)){
            if(isValid) {
                loginbtn.isEnabled=true
                loginbtn.backgroundTintList= ContextCompat.getColorStateList(this,R.color.primary_color)
            } else {
                loginbtn.isEnabled=false
                loginbtn.backgroundTintList= ContextCompat.getColorStateList(this,android.R.color.darker_gray)
            }
        }

        loginbtn.setOnClickListener {
            val mail=et_mail.text.toString().trim()
            val pass=et_password.text.toString().trim()
            loginUser(mail,pass)
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }

        tv_havent_account.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        tv_forgot_password.setOnClickListener {
            startActivity(Intent(this,ResetPasswordActivity::class.java))
        }
    }

    private fun loginUser(mail: String, pass: String) {
        auth.signInWithEmailAndPassword(mail,pass)
            .addOnCompleteListener(this){ login ->
                if(login.isSuccessful){
                    Intent(this,HomeActivity::class.java).also {
                        it.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        Toast.makeText(this,"Login Successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this,login.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showTextMinimalAlert(isNotValid: Boolean,text:String) {
        if(text == "Email/Username")
            et_mail.error=if(isNotValid) "$text cannot be empty!" else null
        else if(text=="Password")
            et_password.error=if(isNotValid) "$text cannot be empty!" else null
    }
}
