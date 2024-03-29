package com.android.aplikasigithubuser.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorite")
data class Favorite(
    @PrimaryKey(true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("username")
    val username: String,

    @ColumnInfo("avatarUrl")
    val avatarUrl: String,

    @ColumnInfo("htmlUrl")
    val htmlUrl: String
)
