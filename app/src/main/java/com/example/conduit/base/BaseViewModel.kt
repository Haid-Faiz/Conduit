package com.example.conduit.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class BaseViewModel(private val baseRepo: BaseRepo) : ViewModel() {

    fun logout() = viewModelScope.launch {
        baseRepo.logout()
    }
}