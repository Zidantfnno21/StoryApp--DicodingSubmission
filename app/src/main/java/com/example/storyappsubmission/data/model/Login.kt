package com.example.storyappsubmission.data.model

import com.google.gson.annotations.SerializedName

data class Login(
    @field:SerializedName("userId")
    val userId: String?,
    @field:SerializedName("name")
    val name: String?,
    @field:SerializedName("token")
    val token: String
)
