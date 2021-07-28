package com.example.conduit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.models.responses.UserResponse
import com.example.conduit.base.BaseViewModel
import com.example.conduit.base.Resource
import com.example.conduit.data.UserPreference
import com.example.conduit.data.repos.UserRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(
    private val userRepo: UserRepo
) : BaseViewModel(userRepo) {

    private var _user: MutableLiveData<Resource<UserResponse>> = MutableLiveData()
    val user: LiveData<Resource<UserResponse>> = _user

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        _user.postValue(Resource.Loading())
        userRepo.loginUser(email, password).let {
            _user.postValue(it)
        }
    }

    fun signupUser(username: String, email: String, password: String) = viewModelScope.launch {
        _user.postValue(Resource.Loading())
        userRepo.signUp(username, email, password).let {
            _user.postValue(it)
        }
    }

    fun updateUser(
        username: String?,
        email: String?,
        bio: String?,
        imageUrl: String?
    ) = viewModelScope.launch {
        _user.postValue(Resource.Loading())
        userRepo.updateUser(username, email, bio, imageUrl)?.let {
            _user.postValue(it)
        }
    }

    fun getCurrentUser(token: String) = viewModelScope.launch {
        userRepo.getCurrentUser(token).let {
            _user.postValue(Resource.Loading())
            _user.postValue(it)
        }
    }

    fun saveAuthToken(token: String) = viewModelScope.launch {
        userRepo.saveAuthToken(token)
    }

//    fun logout() {
//        _user.postValue(null)
//    }

}