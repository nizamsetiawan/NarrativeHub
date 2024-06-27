package com.intermediate.storyapp.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.intermediate.storyapp.data.pref.UserPreferences
import com.intermediate.storyapp.databinding.ActivityMainBinding
import com.intermediate.storyapp.view.dashboard.DashboardActivity
import com.intermediate.storyapp.view.login.LoginViewModel
import com.intermediate.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        checkUserSession()

    }
    private fun checkUserSession() {
        val token = UserPreferences.userToken
        val intent = if (token.isNotEmpty()) {
            Intent(this, DashboardActivity::class.java)
        } else {
            Intent(this, WelcomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}