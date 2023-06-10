package com.example.storyappsubmission.ui.auth

import androidx.lifecycle.ViewModel
import com.example.storyappsubmission.data.StoryRepository

class LoginViewModel(private val storyRepository : StoryRepository): ViewModel() {
    fun login(email: String, password: String) = storyRepository.logIn(email, password)
}