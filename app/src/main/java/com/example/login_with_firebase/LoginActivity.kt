package com.example.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("CheckResult")
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userNameStream = RxTextView.textChanges(et_mail)
            .skipInitialValue()
            .map { username -> username.isEmpty() }
            .subscribe { showTextMinimalAlert(it,"Email/Username") }

        val passwordStream = RxTextView.textChanges(et_password)
            .skipInitialValue()
            .map { password -> password.isEmpty() }
            .subscribe { showTextMinimalAlert(it,"Password") }


        // Button Enable/Disable
        val invalidFieldsStream = Observable.combineLatest(userNameStream,passwordStream,
            { usernameInvalid: Boolean, passwordInvalid: Boolean
                -> !usernameInvalid && !passwordInvalid
            })

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
            startActivity(Intent(this,HomeActivity::class.java))
        }

        tv_havent_account.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }

    private fun showTextMinimalAlert(isNotValid: Boolean,text:String) {
        if(text == "Email/Username")
            et_mail.error=if(isNotValid) "$text cannot be empty!" else null
        else if(text=="Password")
            et_password.error=if(isNotValid) "$text cannot be empty!" else null
    }

}
