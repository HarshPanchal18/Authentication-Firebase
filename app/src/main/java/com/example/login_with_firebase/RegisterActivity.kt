package com.example.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_register.*

@Suppress("CheckResult")
class RegisterActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Authentication
        auth=FirebaseAuth.getInstance()

        // Validation starts here
        val nameStream=RxTextView.textChanges(et_fullname)
            .skipInitialValue()
            .map { name -> name.isEmpty() }
        nameStream.subscribe{ showNameExistAlert(it) }

        val emailStream=RxTextView.textChanges(et_mail)
            .skipInitialValue()
            .map { mail -> !Patterns.EMAIL_ADDRESS.matcher(mail).matches() }
        emailStream.subscribe{ showEmailValidAlert(it) }

        val userNameStream=RxTextView.textChanges(et_username)
            .skipInitialValue()
            .map { username -> username.length < 6 }
        userNameStream.subscribe { showTextMinimalAlert(it,"Username") }

        val passwordStream = RxTextView.textChanges(et_password)
            .skipInitialValue()
            .map { password -> password.length < 8 }
        passwordStream.subscribe { showTextMinimalAlert(it,"Password") }

        val passwordConfirmStream =
            Observable.merge(
                RxTextView.textChanges(et_password).skipInitialValue()
                    .map { password -> password.toString() != et_conf_password.text.toString() },

                RxTextView.textChanges(et_conf_password).skipInitialValue()
                    .map { confirmPassword -> confirmPassword.toString() != et_password.text.toString() })
        passwordConfirmStream.subscribe { showPasswordConfirmAlert(it) }

        // Button Enable/Disable
        val invalidFieldsStream : Observable<Boolean> = Observable.combineLatest(
                nameStream,
                emailStream,
                userNameStream,
                passwordStream,
                passwordConfirmStream
        ) {
                nameInvalid: Boolean,
                emailInvalid: Boolean,
                usernameInvalid: Boolean,
                passwordInvalid: Boolean,
                cpasswordInvalid: Boolean
            ->
            !nameInvalid && !emailInvalid && !usernameInvalid && !passwordInvalid && !cpasswordInvalid
        }

        invalidFieldsStream.subscribe { isValid:Boolean ->
            if (isValid) {
                registerbtn.isEnabled=true
                registerbtn.backgroundTintList=ContextCompat.getColorStateList(this,R.color.primary_color)
            } else {
                registerbtn.isEnabled=false
                registerbtn.backgroundTintList=ContextCompat.getColorStateList(this,android.R.color.darker_gray)
            }
        }

        registerbtn.setOnClickListener {
            val mail=et_mail.text.toString().trim()
            val pass=et_password.text.toString().trim()
            registerUser(mail,pass)
        }

        tv_have_account.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }

    private fun registerUser(mail: String, pass: String) {
        auth.createUserWithEmailAndPassword(mail,pass)
            .addOnCompleteListener(this) {
                if(it.isSuccessful){
                    startActivity(Intent(this,LoginActivity::class.java))
                    Toast.makeText(this,"Registered Successfully",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this,it.exception?.message,Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showNameExistAlert(isNotValid:Boolean) {
        et_fullname.error=if(isNotValid) "Name cannot be empty" else null
    }

    private fun showTextMinimalAlert(isNotValid: Boolean,text:String) {
        if(text=="Username")
            et_username.error=if(isNotValid) "$text must be more than 6 letters!" else null
        else if(text=="Password")
            et_password.error=if(isNotValid) "$text must be more than 8 letters!" else null
    }

    private fun showEmailValidAlert(isNotValid: Boolean) {
        et_mail.error=if(isNotValid) "Email is not valid!" else null
    }

    private fun showPasswordConfirmAlert(isNotValid: Boolean) {
        et_password.error=if(isNotValid) "Password are not the same" else null
    }
}
