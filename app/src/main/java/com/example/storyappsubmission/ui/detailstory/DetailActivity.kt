package com.example.storyappsubmission.ui.detailstory

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.model.Story
import com.example.storyappsubmission.databinding.ActivityDetailBinding
import com.example.storyappsubmission.helper.ViewModelFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val id = intent.getStringExtra(EXTRA_USERNAME).toString()
        if (id.isNotEmpty()) {
            viewModel.getDetailStory(id).observe(this){result->
                if (result!=null){
                    when(result){
                        is com.example.storyappsubmission.data.Result.Loading -> {
                            showLoading(true)
                        }
                        is com.example.storyappsubmission.data.Result.Success -> {
                            showLoading(false)
                            setStoryDetail(result.data)
                        }
                        is com.example.storyappsubmission.data.Result.Error -> {
                            showLoading(false)
                            Log.e(ContentValues.TAG , result.error)
                            Toast.makeText(this@DetailActivity , R.string.message_fetch_list , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setStoryDetail(data : Story) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(data.photoUrl)
                .into(ivStory)
            tvUsername.text = data.name
            tvTimestamp.text = data.createdAt
            tvDescription.text = data.description
        }
    }

    private fun showLoading(loading : Boolean) {
        binding.progressBar3.isVisible = loading
        if (loading){
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE , WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    companion object{
        const val EXTRA_USERNAME = "username"
    }
}