package com.example.storyappsubmission.data.response

import com.example.storyappsubmission.data.model.Login
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean?,
    @field:SerializedName("message")
    val message: String?,
    @field:SerializedName("loginResult")
    val loginResult: Login
)
