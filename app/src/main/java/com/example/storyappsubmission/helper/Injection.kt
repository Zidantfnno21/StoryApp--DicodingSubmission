package com.example.storyappsubmission.helper

import android.content.Context
import com.example.storyappsubmission.data.StoryRepository
import com.example.storyappsubmission.data.api.ApiConfig
import com.example.storyappsubmission.data.database.StoryDatabase

object Injection {
    fun provideRepository(context : Context): StoryRepository {
        val apiService = ApiConfig.getApiService(context)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository(apiService, database)
    }
}