package com.intermediate.storyapp.data

import com.intermediate.storyapp.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class RemoteRepository(private val api : ApiService) {
    suspend fun login(email : String, password : String) = api.login(email, password)
    suspend fun register(username : String, email : String, password : String) = api.register(username, email, password)
    suspend fun getStory(token : String, page : Int, size : Int) = api.getStory(token, page, size)
    suspend fun detailStory(token : String, id : String) = api.detailStory(token, id)
    suspend fun newStory(token : String, fileImage : MultipartBody.Part, description : RequestBody) = api.newStory(token, fileImage, description)
    suspend fun getLocation(token : String) = api.storyLocation(token)

}