package com.example.storyappsubmission.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.storyappsubmission.data.api.ApiService
import com.example.storyappsubmission.data.database.StoryDatabase
import com.example.storyappsubmission.data.model.Story
import com.example.storyappsubmission.data.response.LoginResponse
import com.example.storyappsubmission.data.response.Response
import com.example.storyappsubmission.data.response.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val apiService : ApiService,
    private val storyDatabase : StoryDatabase
) {
    fun getStory() : LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ) ,
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService) ,
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getStoryLocation() : LiveData<Result<StoryListResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStoryLocation(1)
                emit(Result.Success(response))
            } catch (e : Exception) {
                Log.e("MapViewModel","getStoryLocation: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getDetailStory(
        id: String
    ) : LiveData<Result<Story>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetail(id)
            val story = response.story
            emit(Result.Success(story))
        } catch (e: Exception) {
            Log.e("DetailViewModel", "getDetailStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postNewStory(
        file : MultipartBody.Part ,
        description : RequestBody,
        lat : Double? = null,
        lon : Double? = null
    ) : LiveData<Result<Response>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadStory(file , description , lat , lon)
            emit(Result.Success(response))
        } catch (e : Exception) {
            Log.e("AddStoryViewModel" , "postNewStory: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun logIn(
        email : String ,
        password : String
    ) : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email,password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("LoginViewModel", "logIn: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<Response>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(name , email , password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "register: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }
}