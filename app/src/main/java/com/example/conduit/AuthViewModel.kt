package com.example.conduit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.models.entities.User
import com.example.conduit.data.UserRepo
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private var _user: MutableLiveData<User?> = MutableLiveData()
    val user: LiveData<User?> = _user

    fun loginUser(email: String, password: String) = viewModelScope.launch {

        UserRepo.loginUser(email, password)?.let {
            _user.postValue(it)
        }
    }

    fun signupUser(username: String, email: String, password: String) = viewModelScope.launch {
        UserRepo.signUp(username, email, password)?.let {
            _user.postValue(it)
        }
    }

}