package com.intermediate.storyapp.view.register

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.intermediate.storyapp.customview.EmailCustomView
import com.intermediate.storyapp.customview.PasswordCustomView
import com.intermediate.storyapp.customview.UsernameCustomView
import com.intermediate.storyapp.data.response.RegisterResponse
import com.intermediate.storyapp.databinding.ActivityRegisterBinding
import com.intermediate.storyapp.view.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModel()
    private lateinit var emailEditText: EmailCustomView
    private lateinit var passwordEditText: PasswordCustomView
    private lateinit var usernameEditText: UsernameCustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViews()
        setupView()
        setupAction()
        playAnimation()
        setupTextWatchers()
    }

    private fun initializeViews() {
        emailEditText = binding.edRegisterEmail
        passwordEditText = binding.edRegisterPassword
        usernameEditText = binding.edRegisterName
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
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

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val name = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() &&
                binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null
            ) {
                binding.progressBar.visibility = View.VISIBLE
                registerViewModel.postRegister(name, email, password)
            } else {
                showAlert("Error", "Mohon lengkapi data dengan benar.")
            }
        }

        registerViewModel.registerResult.observe(this, Observer { response ->
            binding.progressBar.visibility = View.GONE
            handleRegisterResponse(response)
        })
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val animSet = AnimatorSet()
        animSet.playTogether(
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100),
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(100)
        )
        animSet.startDelay = 100
        animSet.start()
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setRegisterButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        usernameEditText.addTextChangedListener(textWatcher)
    }

    private fun setRegisterButtonState() {
        val name = usernameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        binding.registerButton.isEnabled =
            name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    private fun handleRegisterResponse(response: RegisterResponse?) {
        if (response != null) {
            if (response.error == false) {
                showAlert("Great Job!", "Sekarang akunmu udah jadi. Buruan login ya!") {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                showAlert("Error", "Email atau password sudah terdaftar.")
            }
        }
    }

    private fun showAlert(title: String, message: String, onPositive: (() -> Unit)? = null) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { _, _ -> onPositive?.invoke() }
            create()
            show()
        }
    }
}

