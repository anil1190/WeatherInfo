package com.example.collabratest.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.collabratest.R
import com.example.collabratest.databinding.ActivityLoginBinding
import com.example.collabratest.uitls.toast
import com.example.collabratest.viewmodals.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var loginViewModal: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginViewModal = ViewModelProvider(this)[LoginViewModel::class.java]
        loginBinding.loginViewModel = loginViewModal

        setObservers()
    }

    private fun setObservers(){
        loginViewModal.loginButton.observe(this) {
            if (it){
                getString(R.string.successful_login).toast(this)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        loginViewModal.signUpButton.observe(this){
            if (it){
                val intent = Intent(this,RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        loginViewModal.isInitialized.observe(this){
            if (it){
                getString(R.string.field_empty).toast(this)
            }
        }
        loginViewModal.invalidEmail.observe(this){
            if (it){
                getString(R.string.email_validation).toast(this)
            }
        }
        loginViewModal.invalidPassword.observe(this){
            if (it){
                getString(R.string.password_not_exist).toast(this)
            }
        }
        loginViewModal.user_not_found.observe(this){
            if (it){
                getString(R.string.user_not_found).toast(this)
            }

        }
    }

}