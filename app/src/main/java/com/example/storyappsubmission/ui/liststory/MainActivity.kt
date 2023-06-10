package com.example.storyappsubmission.ui.liststory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.ActivityMainBinding
import com.example.storyappsubmission.helper.SessionPreferences
import com.example.storyappsubmission.helper.ViewModelFactory
import com.example.storyappsubmission.ui.adapter.LoadingStateAdapter
import com.example.storyappsubmission.ui.adapter.StoryAdapter
import com.example.storyappsubmission.ui.addstory.AddStoryActivity
import com.example.storyappsubmission.ui.auth.LoginActivity
import com.example.storyappsubmission.ui.maps.MapsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var adapterMain : StoryAdapter
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onStart() {
        super.onStart()
        adapterMain.refresh()
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setUserData()

        binding.floatingActionButton2.setOnClickListener {
            val intent = Intent(this@MainActivity , AddStoryActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.story.observe(this) { result ->
            showLoading(true)
            if (result != null) {
                showLoading(false)
                adapterMain.submitData(lifecycle , result)
                adapterMain.notifyItemRangeChanged(0 , adapterMain.itemCount)
            }
        }
    }

    override fun onCreateOptionsMenu(menu : Menu) : Boolean {
        menuInflater.inflate(R.menu.main_menu , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.logout)
                builder.setMessage(R.string.alert_logout)
                builder.setIcon(R.mipmap.ic_launcher_round)
                builder.setPositiveButton("Yes") { _ , _ ->
                    SessionPreferences().logOut(this)
                    val intent = Intent(this@MainActivity , LoginActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                builder.setNegativeButton("Cancel") { dialog , _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }
            R.id.menu_maps -> {
                val intent = Intent(this@MainActivity , MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData() {
        adapterMain = StoryAdapter()
        binding.rvMain.apply {
            adapter = adapterMain
            setHasFixedSize(false)
            smoothScrollToPosition(0)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        binding.rvMain.adapter = adapterMain.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapterMain.retry()
            }
        )
    }

    private fun showLoading(loading : Boolean) {
        binding.progressBar3.isVisible = loading
    }

}