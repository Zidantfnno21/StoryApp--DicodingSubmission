package com.example.storyappsubmission.ui.detailstory

import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.StoryRepository

class DetailViewModel(private val storyRepository : StoryRepository): ViewModel() {
    fun getDetailStory(id: String) = storyRepository.getDetailStory(id)
}