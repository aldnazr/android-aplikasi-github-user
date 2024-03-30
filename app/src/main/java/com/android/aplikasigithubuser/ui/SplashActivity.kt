package com.android.aplikasigithubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.aplikasigithubuser.R
import com.android.aplikasigithubuser.viewmodel.SettingsViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val LAUNCH_TIME = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        settingsViewModel.getThemeSettings().observe(this) {
            val themeMode: Int = when (it) {
                0 -> AppCompatDelegate.MODE_NIGHT_YES
                1 -> AppCompatDelegate.MODE_NIGHT_NO
                2 -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                else -> {
                    settingsViewModel.saveThemeSettings(2)
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
            AppCompatDelegate.setDefaultNightMode(themeMode)
        }

        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, LAUNCH_TIME)

        hideSystemBar()
    }

    private fun hideSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}