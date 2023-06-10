package com.example.storyappsubmission.ui.maps

import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.StoryRepository

class MapsViewModel(private val storyRepository : StoryRepository): ViewModel() {
    fun getUserLocation() = storyRepository.getStoryLocation()
}