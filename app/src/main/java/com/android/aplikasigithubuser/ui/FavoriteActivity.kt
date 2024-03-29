package com.android.aplikasigithubuser.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.aplikasigithubuser.adapter.RecyclerAdapterFavorite
import com.android.aplikasigithubuser.viewmodel.FavoriteViewModel
import com.android.aplikasigithubuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFavoriteBinding.inflate(layoutInflater)
    }
    private val favoriteModel by viewModels<FavoriteViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            finish()
        }

        val adapter = RecyclerAdapterFavorite()
        binding.recyclerView.adapter = adapter

        favoriteModel.readAllData.observe(this) {
            adapter.setList(it)
            emptyState(it.isEmpty())
        }
    }

    private fun emptyState(isEmpty: Boolean) {
        val showVisibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.included.emptyTitle.visibility = showVisibility
        binding.included.emptyText.apply {
            visibility = showVisibility
            text = "No favorite"
        }
    }
}