package com.example.storyappsubmission.ui.auth

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.storyappsubmission.data.response.LoginResponse
import com.example.storyappsubmission.databinding.ActivityLoginBinding
import com.example.storyappsubmission.helper.SessionPreferences
import com.example.storyappsubmission.helper.ViewModelFactory
import com.example.storyappsubmission.ui.liststory.MainActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val window : Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.WHITE
        }

        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        binding.tvRegister.setOnClickListener {
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.etName.text.toString()
            val pass = binding.etPassword.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            
            if (email.isNotEmpty() && pass.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length >= 8){
                loginViewModel.login(email,pass).observe(this) { result->
                    if (result != null) {
                        when (result) {
                            is com.example.storyappsubmission.data.Result.Loading -> {
                                showLoading(true)
                            }
                            is com.example.storyappsubmission.data.Result.Success -> {
                                showLoading(false)
                                logInGenerate(result.data)
                            }
                            is com.example.storyappsubmission.data.Result.Error -> {
                                showLoading(false)
                                Log.e(TAG, result.error)
                                Toast.makeText(this@LoginActivity , R.string.message_failed_login , Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }else{
                Toast.makeText(this@LoginActivity , R.string.message_form_alert  , Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logInGenerate(data : LoginResponse) {
        if(data.error == true) {
            Toast.makeText(this@LoginActivity , R.string.message_failed_login , Toast.LENGTH_SHORT).show()
        } else {
            SessionPreferences().saveToken(data.loginResult.token , applicationContext)
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
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

    override fun onStart() {
        super.onStart()
        if (SessionPreferences().isTokenAvailable(applicationContext)){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}