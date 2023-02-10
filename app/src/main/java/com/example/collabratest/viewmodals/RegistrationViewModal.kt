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
class RegistrationViewModal @Inject constructor(private val registrationRepository: RegistrationRepository) :
    ViewModel(),Observable{

    @Bindable
    val userName = MutableLiveData<String>()
    @Bindable
    val userEmail = MutableLiveData<String>()
    @Bindable
    val userPassword = MutableLiveData<String>()
    @Bindable
    val confirmPassword = MutableLiveData<String>()
    var dataInserted = MutableLiveData(false)
    var isInitialized = MutableLiveData(false)
    var invalidPassword = MutableLiveData(false)
    var invalidEmail = MutableLiveData(false)
    var passwordExist = MutableLiveData(false)
    private var userList = mutableListOf<UserEntity>()


    fun clickOnSignUpButton(){
       val name = userName.value
       val email = userEmail.value
       val password = userPassword.value
       val confirmPassword = confirmPassword.value

       if (name.isNullOrBlank() || email.isNullOrBlank() || password.isNullOrBlank() || confirmPassword.isNullOrBlank()){
           isInitialized.value = true
       }else{
           if (password != confirmPassword){
               invalidPassword.value = true
           }else if (!isValidEmail(email)!!){
               invalidEmail.value = true
           }else{
               viewModelScope.launch {
                  try {
                      userList = registrationRepository.getAllUsers()
                      if (userList.size != 0){
                          for (i in 0 until userList.size){
                              if (userList[i].userPassword == password){
                                  passwordExist.value = true
                              }
                          }
                          if (passwordExist.value == false){
                              registrationRepository.insertUser(UserEntity(0,name,email,password))
                              dataInserted.value = true
                          }
                      }else{
                          registrationRepository.insertUser(UserEntity(0,name,email,password))
                          dataInserted.value = true
                      }
                      passwordExist.value = false
                  }catch (e:Exception){
                      e.stackTrace
                  }
               }
           }
       }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}