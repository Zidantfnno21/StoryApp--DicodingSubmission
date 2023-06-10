package com.example.storyappsubmission.ui.addstory

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.storyappsubmission.R
import com.example.storyappsubmission.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class AddStoryActivityTest{

    @get:Rule
    val activity = ActivityScenarioRule(AddStoryActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        Intents.init()

        val result = createImageCaptureActivityResultStub()

        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)

        Intents.release()
    }

    @Test
    fun addNewStory_Success() {
        val description = "This Is description dummy"
        onView(withId(R.id.etDescription)).perform(click(), typeText(description), closeSoftKeyboard())

        onView(withId(R.id.tvAdd)).perform(click())

        onView(withText("Select Option")).check(matches(isDisplayed()))

        onView(withText("Take a picture")).perform(click())

        onView(withId(R.id.btUpload)).perform()
    }

    private fun createImageCaptureActivityResultStub() : Instrumentation.ActivityResult {
        val bundle = Bundle()
        bundle.putParcelable(
            AddStoryActivity.CAMERA_X_RESULT.toString() , BitmapFactory.decodeResource(
            InstrumentationRegistry.getInstrumentation().targetContext.resources,
            R.id.ivStory
        ))
        val resultData = Intent()
        resultData.putExtras(bundle)

        return Instrumentation.ActivityResult(Activity.RESULT_OK , resultData)
    }

}