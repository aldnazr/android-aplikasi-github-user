package com.android.aplikasigithubuser.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.aplikasigithubuser.R
import com.android.aplikasigithubuser.databinding.ActivitySettingsBinding
import com.android.aplikasigithubuser.viewmodel.SettingsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class SettingsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySettingsBinding.inflate(layoutInflater)
    }
    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val options = arrayOf("Aktif", "Nonaktif", "Sistem")
    private var themeMode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settingsViewModel.getThemeSettings().observe(this) { bool ->
            bool?.let {
                themeMode = it
                setThemeMode(it)
            }

        }

        with(binding) {
            cardView.setOnClickListener {
                showDialog()
            }
            button.setOnClickListener {
                showDialog()
            }
            materialToolbar.setNavigationOnClickListener {
                finish()
            }
        }
    }

    private fun setThemeMode(mode: Int) {
        val themePicked = when (mode) {
            0 -> AppCompatDelegate.MODE_NIGHT_YES
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            2 -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(themePicked)
        binding.button.text = options[mode]
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(R.string.setting_dark_theme))
            setSingleChoiceItems(options, themeMode!!) { _, which ->
                themeMode = which
            }
            setPositiveButton("OK") { dialog, _ ->
                settingsViewModel.saveThemeSettings(themeMode!!)
                dialog.dismiss()
            }
            show()
        }
    }
}