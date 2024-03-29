package com.android.aplikasigithubuser.retrofit

import com.android.aplikasigithubuser.response.ResponseDetailUser
import com.android.aplikasigithubuser.response.ResponseFollowItem
import com.android.aplikasigithubuser.response.ResponseGithubUsers
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    fun getResponseGithubUsers(@Query("q") username: String): Call<ResponseGithubUsers>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<ResponseDetailUser>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<ResponseFollowItem>>

    @GET("/users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<ResponseFollowItem>>
}