package com.android.aplikasigithubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.aplikasigithubuser.response.ItemsItem
import com.android.aplikasigithubuser.response.ResponseDetailUser
import com.android.aplikasigithubuser.response.ResponseFollowItem
import com.android.aplikasigithubuser.response.ResponseGithubUsers
import com.android.aplikasigithubuser.retrofit.ApiConfig
import org.jetbrains.annotations.Nullable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val TAG = "MainViewModel"

    private val _isFailedLoad = MutableLiveData<Boolean>()
    val isFailedLoad: LiveData<Boolean> = _isFailedLoad

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUsers = MutableLiveData<ArrayList<ItemsItem>>()
    val getListUsers: LiveData<ArrayList<ItemsItem>> = _listUsers

    private val _detailUser = MutableLiveData<ResponseDetailUser>()
    val getDetailUser: LiveData<ResponseDetailUser> = _detailUser

    private val _listFollowers = MutableLiveData<ArrayList<ResponseFollowItem>>()
    val getListFollowers: LiveData<ArrayList<ResponseFollowItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<ArrayList<ResponseFollowItem>>()
    val getListFollowing: LiveData<ArrayList<ResponseFollowItem>> = _listFollowing

    fun setListUsers(@Nullable username: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getResponseGithubUsers(username ?: "username")
        client.enqueue(object : Callback<ResponseGithubUsers> {
            override fun onResponse(
                call: Call<ResponseGithubUsers>, response: Response<ResponseGithubUsers>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    return
                }
                _listUsers.value = response.body()?.items?.let { ArrayList(it) }
            }

            override fun onFailure(call: Call<ResponseGithubUsers>, t: Throwable) {
                _isFailedLoad.value = true
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(
                call: Call<ResponseDetailUser>, response: Response<ResponseDetailUser>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    return
                }
                _detailUser.value = response.body()
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                _isFailedLoad.value = true
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setListFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<ArrayList<ResponseFollowItem>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseFollowItem>>,
                response: Response<ArrayList<ResponseFollowItem>>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    return
                }
                _listFollowers.value = response.body()?.let { ArrayList(it) }
            }

            override fun onFailure(call: Call<ArrayList<ResponseFollowItem>>, t: Throwable) {
                _isFailedLoad.value = true
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setListFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<ArrayList<ResponseFollowItem>> {
            override fun onResponse(
                call: Call<ArrayList<ResponseFollowItem>>,
                response: Response<ArrayList<ResponseFollowItem>>
            ) {
                _isLoading.value = false
                if (!response.isSuccessful) {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    return
                }
                _listFollowing.value = response.body()?.let { ArrayList(it) }
            }

            override fun onFailure(call: Call<ArrayList<ResponseFollowItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}