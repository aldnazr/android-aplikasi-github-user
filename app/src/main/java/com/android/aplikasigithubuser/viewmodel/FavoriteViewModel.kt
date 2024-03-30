package com.android.aplikasigithubuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.aplikasigithubuser.database.Favorite
import com.android.aplikasigithubuser.database.FavoriteDatabase
import com.android.aplikasigithubuser.database.FavoriteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FavoriteRepository
    val readAllData: LiveData<List<Favorite>>

    init {
        val favoriteDao = FavoriteDatabase.getInstance(application).favoriteDao()
        repository = FavoriteRepository(favoriteDao)
        readAllData = repository.readAllData()
    }

    fun insert(favorite: Favorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(favorite)
        }
    }

    fun delete(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(username)
        }
    }
}