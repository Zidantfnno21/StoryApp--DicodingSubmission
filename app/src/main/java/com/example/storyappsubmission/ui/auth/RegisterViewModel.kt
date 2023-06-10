package com.example.storyappsubmission.ui.auth

import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.StoryRepository

class RegisterViewModel(private val storyRepository : StoryRepository): ViewModel() {
    fun register(username: String, email: String, password: String) = storyRepository.register(username,email,password)
}