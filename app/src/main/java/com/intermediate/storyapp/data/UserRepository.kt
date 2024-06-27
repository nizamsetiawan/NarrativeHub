package com.intermediate.storyapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.intermediate.storyapp.data.pref.UserPreferences
import com.intermediate.storyapp.data.response.DetailResponse
import com.intermediate.storyapp.data.response.ListStoryItem
import com.intermediate.storyapp.data.response.LoginResponse
import com.intermediate.storyapp.data.response.RegisterResponse
import com.intermediate.storyapp.data.response.StoryAddResponse
import com.intermediate.storyapp.data.response.StoryResponse
import com.intermediate.storyapp.view.dashboard.StoryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UserRepository(private val remoteRepository: RemoteRepository) {

    fun login(email: String, password: String): Flow<LoginResponse> = flow {
        try {
            val response = remoteRepository.login(email, password)
            if (response.error == true) {
                emit(response)
            } else {
                val loginResult = response.loginResult
                if (loginResult != null) {
                    UserPreferences.setLoginCredentials(loginResult)
                }
                emit(response)
            }
        } catch (e: Exception) {
            emit(LoginResponse(error = true, message = e.message ?: "Unknown error"))
        }
    }

    fun register(name: String, email: String, password: String): Flow<RegisterResponse> = flow {
        try {
            val response = remoteRepository.register(name, email, password)
            if (response.error == true) {
                emit(response)
            } else {
                emit(response)
            }
        } catch (e: Exception) {
            emit(RegisterResponse(error = true, message = e.message ?: "Unknown error"))
        }
    }

    fun getStories(token: String): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(remoteRepository, token) }
        ).flow
    }

    fun detailStory(token: String, id: String): Flow<DetailResponse> = flow {
        try {
            val response = remoteRepository.detailStory("Bearer $token", id)
            if (response.error == true) {
                emit(response)
            } else {
                emit(response)
            }
        } catch (e: Exception) {
            emit(DetailResponse(error = true, message = e.message ?: "Unknown error"))
        }
    }

    fun newStory(token: String, file: File, description: String): Flow<StoryAddResponse> = flow {
        try {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestFile = file.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData("photo", file.name, requestFile)
            val response = remoteRepository.newStory("Bearer $token", multipartBody, requestBody)
            emit(response)
        } catch (e: Exception) {
            emit(StoryAddResponse(error = true, message = e.message ?: "Unknown error"))
        }
    }

    fun getStoriesWithLocation(token: String): Flow<StoryResponse> = flow {
        try {
            val response = remoteRepository.getLocation("Bearer $token")
            if (response.error == true) {
                emit(response)
            } else {
                emit(response)
            }
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error")
        }
    }

}



