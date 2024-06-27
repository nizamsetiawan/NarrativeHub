package com.intermediate.storyapp.data.retrofit

import com.intermediate.storyapp.data.response.DetailResponse
import com.intermediate.storyapp.data.response.LoginResponse
import com.intermediate.storyapp.data.response.RegisterResponse
import com.intermediate.storyapp.data.response.StoryAddResponse
import com.intermediate.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
       @Field("email") email: String,
       @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
       @Field("name") name: String,
       @Field("email") email: String,
       @Field("password") password: String
    ): RegisterResponse


    @Multipart
    @POST("stories")
    suspend fun newStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): StoryAddResponse

    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): DetailResponse

    @GET("stories")
    suspend fun storyLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ): StoryResponse

}