package com.example.storyappsubmission.utils

import com.example.storyappsubmission.data.model.Story
import com.example.storyappsubmission.data.response.StoryListResponse

object DataDummy {
    fun generateDummyStory(): StoryListResponse {
        return StoryListResponse(
            error = false,
            message = "Success",
            listStory = arrayListOf(
                Story(
                    "id_story",
                    "daijoubu",
                    "dummyStory Description goes Hard",
                    "photoUrl",
                    "createdAt",
                    0.01, 0.02
                )
            )
        )
    }

    fun generateErrorDummyStory(): StoryListResponse {
        return StoryListResponse(
            error = true,
            message = "error",
            listStory = arrayListOf(
                Story(
                    id = "id",
                    name = "name",
                    description = "description",
                    photoUrl = "photoUrl",
                    createdAt = "createdAt",
                    lat = 0.01,
                    lon = 0.01
                )
            )
        )
    }
}