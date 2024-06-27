package com.intermediate.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.intermediate.storyapp.customview.EmailCustomView
import com.intermediate.storyapp.customview.PasswordCustomView
import com.intermediate.storyapp.databinding.ActivityLoginBinding
import com.intermediate.storyapp.view.dashboard.DashboardActivity
import com.intermediate.storyapp.view.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var emailEditText: EmailCustomView
    private lateinit var passwordEditText: PasswordCustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViews()
        setupView()
        playAnimation()
        setupAction()
    }

    private fun initializeViews() {
        emailEditText = binding.edLoginEmail
        passwordEditText = binding.edLoginPassword
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animSet = AnimatorSet()
        animSet.playTogether(
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        )
        animSet.startDelay = 100
        animSet.start()
    }

    private fun setupAction() {
        binding.apply {
            loginButton.setOnClickListener { login() }
            messageTextView.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
        setupTextWatchers()
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setMyButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
    }

    private fun setMyButtonState() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        binding.loginButton.isEnabled = email.isNotEmpty() && password.isNotEmpty()
    }

    private fun login() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            loginViewModel.postLogin(email, password)
            observeLoginResult()
        } else {
            Toast.makeText(this, "Silakan isi email dan password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            if (result.error == false) {
                showAlert(
                    "Great Job!",
                    "Kamu udah berhasil login nih. Selamat menggunakan Story Application!"
                )
            } else {
                Toast.makeText(this, "Email atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, which ->
                startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                finish()
            }
            create()
            show()
        }
    }
}
