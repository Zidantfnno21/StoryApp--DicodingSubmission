package com.example.storyappsubmission.ui.liststory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyappsubmission.data.StoryRepository
import com.example.storyappsubmission.data.model.Story

class MainViewModel(storyRepository : StoryRepository): ViewModel() {
    val story: LiveData<PagingData<Story>> = storyRepository.getStory().cachedIn(viewModelScope)
}