package com.android.aplikasigithubuser.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.aplikasigithubuser.R
import com.android.aplikasigithubuser.adapter.RecyclerAdapterMain
import com.android.aplikasigithubuser.databinding.ActivityMainBinding
import com.android.aplikasigithubuser.response.ItemsItem
import com.android.aplikasigithubuser.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.search.SearchView.TransitionState


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPress()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            val decoration = MaterialDividerItemDecoration(this@MainActivity, RecyclerView.VERTICAL)
            recyclerView.addItemDecoration(decoration)

            searchView.addTransitionListener { searchView, previousState, newState ->
                if (newState == TransitionState.HIDING) {
                    mainViewModel.setListUsers(null)
                }
            }

            searchView.editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    val searchText = p0.toString()
                    if (searchText.isNotEmpty()) {
                        mainViewModel.setListUsers(searchText)
                    } else {
                        mainViewModel.setListUsers(null)
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }
            })

            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favorit -> startActivity(
                        Intent(
                            this@MainActivity, FavoriteActivity::class.java
                        )
                    )

                    R.id.settings -> startActivity(
                        Intent(
                            this@MainActivity, SettingsActivity::class.java
                        )
                    )
                }
                true
            }
        }
    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback {
            if (binding.searchView.isShowing) {
                binding.searchView.hide()
                mainViewModel.setListUsers(null)
                return@addCallback
            }
            finish()
        }
        with(mainViewModel) {
            setListUsers(null)
            getListUsers.observe(this@MainActivity) {
                setRecyclerView(it)
                emptyState(it.isEmpty())
            }
            isLoading.observe(this@MainActivity) {
                displayProgressBar(it)
            }
            isFailedLoad.observe(this@MainActivity) {
                displayAlertDialog(it, this@MainActivity)
            }
        }
    }

    private fun setRecyclerView(listUser: ArrayList<ItemsItem>) {
        binding.recyclerView.adapter = RecyclerAdapterMain(listUser)
        binding.recyclerViewSearch.adapter = RecyclerAdapterMain(listUser)
    }

    private fun displayProgressBar(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun displayAlertDialog(isFailedLoad: Boolean, context: Context) {
        if (isFailedLoad) {
            MaterialAlertDialogBuilder(context).setTitle("No Connection")
                .setMessage("Failed to load data").setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }.create().show()
        }
    }

    private fun emptyState(isEmpty: Boolean) {
        val showVisibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.include.emptyTitle.visibility = showVisibility
        binding.include.emptyText.apply {
            visibility = showVisibility
            text = "Empty Users"
        }
    }
}