package com.example.collabratest.viewmodals

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collabratest.modals.UserEntity
import com.example.collabratest.repository.RegistrationRepository
import com.example.collabratest.uitls.Constants.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val registrationRepository: RegistrationRepository):
 ViewModel(),Observable{

    @Bindable
    val userEmail = MutableLiveData<String>()
    @Bindable
    val userPassword = MutableLiveData<String>()
    var loginButton = MutableLiveData(false)
    var signUpButton = MutableLiveData(false)
    var isInitialized = MutableLiveData(false)
    var invalidEmail = MutableLiveData(false)
    var invalidPassword = MutableLiveData(false)
    val user_not_found = MutableLiveData(false)
    private var userList = mutableListOf<UserEntity>()


    fun clickOnLoginButton(){
       val email = userEmail.value
       val password = userPassword.value
       if (email.isNullOrEmpty() || password.isNullOrEmpty()){
          isInitialized.value = true
       }else if (!isValidEmail(email)!!){
           invalidEmail.value = true
       }else{
          viewModelScope.launch{
              try {
                  userList = registrationRepository.getAllUsers()
                  if (userList.size != 0){
                      for (i in 0 until userList.size){
                          if (userList[i].userPassword == password && userList[i].userEmail == email){
                              loginButton.value = true
                          }
                      }
                      if (loginButton.value == false){
                          invalidPassword.value = true
                      }
                  }else{
                      user_not_found.value = true
                  }
              }catch (e:Exception){
                  e.stackTrace
              }
          }
       }

    }

   fun clickOnSignUpButton(){
      signUpButton.value = true
   }
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}