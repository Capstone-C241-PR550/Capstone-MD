package com.bangkit.financeguard.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.bangkit.financeguard.databinding.ActivityWelcomeBinding
import com.bangkit.financeguard.view.login.LoginActivity
import com.bangkit.financeguard.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setupView()
        setupAction()
        playAnimation()
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
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        // Animasi gambar yang bergerak dari kiri ke kanan
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // Animasi alpha untuk TextView dan Button
        val register = ObjectAnimator.ofFloat(binding.registerTextView, View.ALPHA, 0f, 1f).setDuration(200)
        val registerButton = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).setDuration(200)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(200)

        // Mengatur AnimatorSet untuk menjalankan animasi secara berurutan
        AnimatorSet().apply {
            playSequentially(
                register,
                registerButton,
                loginButton
            )
            startDelay = 100
        }.start()
    }
}