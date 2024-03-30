package com.android.aplikasigithubuser.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.aplikasigithubuser.R
import com.android.aplikasigithubuser.adapter.PagerAdapter
import com.android.aplikasigithubuser.database.Favorite
import com.android.aplikasigithubuser.databinding.ActivityDetailUserBinding
import com.android.aplikasigithubuser.viewmodel.FavoriteViewModel
import com.android.aplikasigithubuser.viewmodel.MainViewModel
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailUserBinding.inflate(layoutInflater)
    }

    private val mainViewModel by viewModels<MainViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    private var isUserFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intentGetUsername = intent.getStringExtra("username").toString()
        val intentGetAvatarUrl = intent.getStringExtra("avatar_url").toString()
        val intentGetHtmlUrl = intent.getStringExtra("htmlUrl").toString()

        with(binding) {
            setSupportActionBar(materialToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = intentGetUsername
            materialToolbar.setNavigationOnClickListener {
                finish()
            }

            with(mainViewModel) {
                setDetailUser(intentGetUsername)
                getDetailUser.observe(this@DetailUserActivity) {
                    Glide.with(this@DetailUserActivity).load(it.avatarUrl).into(circleImageView)
                    name.text = it.name
                    username.text = it.login
                    follower.text = getString(R.string.followers, it.followers)
                    following.text = getString(R.string.following, it.following)
                }
                isLoading.observe(this@DetailUserActivity) {
                    showProgress(it)
                }
                isFailedLoad.observe(this@DetailUserActivity) {
                    displayAlertDialog(it, this@DetailUserActivity)
                }
                isScrolled.observe(this@DetailUserActivity) {
                    if (it) fab.show() else fab.hide()
                }
            }

            favoriteViewModel.readAllData.observe(this@DetailUserActivity) { favoriteList ->
                favoriteList.map {
                    isUserFavorite = it.username == intentGetUsername
                }
                setFabIcon(isUserFavorite)
            }

            fab.setOnClickListener {
                isUserFavorite = if (isUserFavorite) {
                    favoriteViewModel.delete(intentGetUsername)
                    false
                } else {
                    favoriteViewModel.insert(
                        Favorite(
                            username = intentGetUsername,
                            avatarUrl = intentGetAvatarUrl,
                            htmlUrl = intentGetHtmlUrl
                        )
                    )
                    true
                }
            }

            // Set Tablayout and ViewPager
            val adapter = PagerAdapter(this@DetailUserActivity)
            viewPager2.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                val tabs = arrayOf("Followers", "Following")
                tab.text = tabs[position]

                val bundle = Bundle().apply {
                    putString("username", intentGetUsername)
                }
                adapter.fragments[position].arguments = bundle
            }.attach()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setFabIcon(isChecked: Boolean) {
        if (isChecked) {
            binding.fab.setImageDrawable(getDrawable(R.drawable.favorite_fill))
        } else {
            binding.fab.setImageDrawable(getDrawable(R.drawable.favorite_outlined))
        }
    }

    private fun showProgress(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun displayAlertDialog(isFailedLoad: Boolean, context: Context) {
        if (isFailedLoad) {
            MaterialAlertDialogBuilder(context).setTitle("No Connection")
                .setMessage("Failed to load data").setPositiveButton("OK") { _, _ ->
                    finish()
                }.create().show()
        }
    }
}