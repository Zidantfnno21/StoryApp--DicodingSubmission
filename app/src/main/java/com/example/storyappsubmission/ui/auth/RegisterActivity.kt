package com.example.storyappsubmission.ui.auth

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.example.storyappsubmission.R
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.helper.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window : Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.WHITE
        }

        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        binding.tvRegister.setOnClickListener {
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val username = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            if (username.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length >= 8) {
                registerViewModel.register(username,email,pass).observe(this) {result->
                    if (result!=null){
                        when(result){
                            is com.example.storyappsubmission.data.Result.Loading -> {
                                showLoading(true)
                            }
                            is com.example.storyappsubmission.data.Result.Success -> {
                                showLoading(false)
                                Toast.makeText(this@RegisterActivity , R.string.message_success_register , Toast.LENGTH_SHORT)
                                    .show()
                                val i = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(i)
                                finish()
                            }
                            is com.example.storyappsubmission.data.Result.Error -> {
                                showLoading(false)
                                Toast.makeText(this@RegisterActivity , R.string.message_failed_register , Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this@RegisterActivity , R.string.message_form_alert , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(loading : Boolean) {
        binding.progressBar3.isVisible = loading
        if (loading){
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

}