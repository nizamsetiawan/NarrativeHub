package com.intermediate.storyapp.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intermediate.storyapp.data.UserRepository
import com.intermediate.storyapp.data.response.DetailResponse
import com.intermediate.storyapp.data.response.StoryAddResponse
import com.intermediate.storyapp.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class StoryViewModel(private val repository: UserRepository) : ViewModel() {


    private val _detailResponse = MutableLiveData<DetailResponse>()
    val detailResponse: LiveData<DetailResponse> get() = _detailResponse

    private val _addStoryResponse = MutableLiveData<StoryAddResponse>()
    val addStoryResponse: LiveData<StoryAddResponse> get() = _addStoryResponse

    private val _locationResponse = MutableLiveData<StoryResponse>()
    val locationResponse: LiveData<StoryResponse> get() = _locationResponse


    fun detailStory(token: String, id: String) {
        viewModelScope.launch {
            try {
                repository.detailStory(token, id).collect { response ->
                    _detailResponse.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addNewStory(
        token: String,
        file: File,
        description: String,
        onSuccess: (StoryAddResponse) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.newStory(token, file, description).collect { response ->
                    onSuccess.invoke(response)
                }
            } catch (e: Exception) {
                onError.invoke(e)
            }
        }
    }

    fun getStoryWithLocation(token: String) {
        viewModelScope.launch {
            try {
                repository.getStoriesWithLocation(token).collect { response ->
                    _locationResponse.value = response
                }
            } catch (e: Exception) {
                    e.printStackTrace()

            }
        }
    }

}
