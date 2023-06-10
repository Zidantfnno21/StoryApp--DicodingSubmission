package com.example.storyappsubmission.data.response

import com.example.storyappsubmission.data.model.Story
import com.google.gson.annotations.SerializedName

data class StoryListResponse(
    @field:SerializedName("error")
    val error: Boolean?,
    @field:SerializedName("message")
    val message: String?,
    @field:SerializedName("listStory")
    val listStory: List<Story>
)
