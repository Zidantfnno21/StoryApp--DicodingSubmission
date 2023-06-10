package com.example.storyappsubmission.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyappsubmission.data.StoryRepository
import com.example.storyappsubmission.ui.addstory.StoryViewModel
import com.example.storyappsubmission.ui.auth.LoginViewModel
import com.example.storyappsubmission.ui.auth.RegisterViewModel
import com.example.storyappsubmission.ui.detailstory.DetailViewModel
import com.example.storyappsubmission.ui.liststory.MainViewModel
import com.example.storyappsubmission.ui.maps.MapsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val storyRepository : StoryRepository): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(storyRepository) as T
        }
        else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storyRepository) as T
        }
        else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(storyRepository) as T
        }
        else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(storyRepository) as T
        }
        else if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(storyRepository) as T
        }
        else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context : Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}