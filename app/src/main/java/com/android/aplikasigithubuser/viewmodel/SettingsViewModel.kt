package com.android.aplikasigithubuser.viewmodel

import DataStoreManager
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val pref = DataStoreManager.getInstance(application.dataStore)

    fun getThemeSettings(): LiveData<Int?> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(mode: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            pref.saveThemeSetting(mode)
        }
    }
}