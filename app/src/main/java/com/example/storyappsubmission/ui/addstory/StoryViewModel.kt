package com.example.storyappsubmission.ui.addstory

import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val storyRepository : StoryRepository): ViewModel() {
    fun postStory(
        file : MultipartBody.Part,
        description: RequestBody,
        lat : Double?,
        lon : Double?
    ) = storyRepository.postNewStory(file, description, lat, lon)
}