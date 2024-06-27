package com.intermediate.storyapp.view.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.intermediate.storyapp.data.UserRepository
import com.intermediate.storyapp.data.response.ListStoryItem
import com.intermediate.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: UserRepository) : ViewModel() {
    private val _storyResponse = MutableLiveData<PagingData<ListStoryItem>>()
    val storyResponse: LiveData<PagingData<ListStoryItem>> get() = _storyResponse

    fun getStory(token: String) {
        viewModelScope.launch {
            try {
                repository.getStories(token).collect { response ->
                    _storyResponse.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}