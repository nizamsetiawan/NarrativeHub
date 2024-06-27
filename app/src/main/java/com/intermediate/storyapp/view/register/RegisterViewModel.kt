package com.intermediate.storyapp.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intermediate.storyapp.data.UserRepository
import com.intermediate.storyapp.data.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> get() = _registerResult

    fun postRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.register(name, email, password).collect { response ->
                _registerResult.value = response
            }
        }
    }
}