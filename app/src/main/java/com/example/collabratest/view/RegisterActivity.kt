package com.example.collabratest.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.collabratest.R
import com.example.collabratest.databinding.ActivityRegisterBinding
import com.example.collabratest.uitls.toast
import com.example.collabratest.viewmodals.RegistrationViewModal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var registrationViewModal: RegistrationViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        registrationViewModal = ViewModelProvider(this)[RegistrationViewModal::class.java]
        registerBinding.registrationViewModal = registrationViewModal

        setObservers()
    }
    private fun setObservers(){
       registrationViewModal.dataInserted.observe(this) {
           if (it) {
               getString(R.string.registration_success).toast(this)
               val intent = Intent(this, MainActivity::class.java)
               startActivity(intent)
               finish()
           }
       }
        registrationViewModal.isInitialized.observe(this){
            if (it){
                getString(R.string.field_empty).toast(this)
            }
        }
        registrationViewModal.invalidPassword.observe(this){
            if (it){
                getString(R.string.password_mathcer).toast(this)
            }
        }
        registrationViewModal.invalidEmail.observe(this){
            if (it){
                getString(R.string.email_validation).toast(this)
            }
        }
        registrationViewModal.passwordExist.observe(this){
            if (it){
                getString(R.string.password_exist).toast(this)
            }
        }
    }
}