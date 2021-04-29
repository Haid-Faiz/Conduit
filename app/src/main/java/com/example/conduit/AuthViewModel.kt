 package com.example.conduit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.models.responses.UserResponse
import com.example.conduit.base.BaseViewModel
import com.example.conduit.base.Resource
import com.example.conduit.data.UserRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(private val userRepo: UserRepo) : BaseViewModel(userRepo) {

    private var _user: MutableLiveData<Resource<out Response<UserResponse>>> = MutableLiveData()
    val user: LiveData<Resource<out Response<UserResponse>>> = _user

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        userRepo.loginUser(email, password).let {
            _user.postValue(it)
        }
    }

    fun signupUser(username: String, email: String, password: String) = viewModelScope.launch {
        userRepo.signUp(username, email, password).let {
            _user.postValue(it)
        }
    }

    fun updateUser(
        username: String?,
        email: String?,
        password: String?,
        bio: String?,
        imageUrl: String?
    ) = viewModelScope.launch {
        userRepo.updateUser(username, email, password, bio, imageUrl)?.let {
            _user.postValue(it)
        }
    }

    fun getCurrentUser(token: String) = viewModelScope.launch {
        userRepo.getCurrentUser(token).let {
            _user.postValue(it)
        }
    }

//    fun logout() {
//        _user.postValue(null)
//    }

}