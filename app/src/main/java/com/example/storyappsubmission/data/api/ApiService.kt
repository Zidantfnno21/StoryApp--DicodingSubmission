package com.example.storyappsubmission.data.api

import com.example.storyappsubmission.data.response.DetailResponse
import com.example.storyappsubmission.data.response.LoginResponse
import com.example.storyappsubmission.data.response.Response
import com.example.storyappsubmission.data.response.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password : String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file : MultipartBody.Part ,
        @Part("description") description : RequestBody ,
        @Part("lat") lat : Double? = null,
        @Part("lon") lon : Double? = null
    ): Response

    @GET("stories")
    suspend fun getStory(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryListResponse

    @GET("stories")
    suspend fun getStoryLocation(
        @Query("location") location: Int,
    ): StoryListResponse

    @GET("stories/{id}")
    suspend fun getDetail(
        @Path("id") id: String
    ): DetailResponse
}