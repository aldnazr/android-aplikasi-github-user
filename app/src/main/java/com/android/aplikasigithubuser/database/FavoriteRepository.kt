package com.android.aplikasigithubuser.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    fun readAllData(): LiveData<List<Favorite>> = favoriteDao.readAllData()

    fun insert(userFavorite: Favorite) {
        favoriteDao.insertData(userFavorite)
    }

    fun delete(username: String) {
        favoriteDao.deleteData(username)
    }
}