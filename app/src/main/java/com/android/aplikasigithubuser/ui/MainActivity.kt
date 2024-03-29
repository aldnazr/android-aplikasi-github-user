package com.android.aplikasigithubuser.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.aplikasigithubuser.R
import com.android.aplikasigithubuser.adapter.RecyclerAdapterMain
import com.android.aplikasigithubuser.databinding.ActivityMainBinding
import com.android.aplikasigithubuser.response.ItemsItem
import com.android.aplikasigithubuser.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textview, _, _ ->
                val searchText = textview.text.toString()
                if (searchText.isNotEmpty()) {
                    mainViewModel.setListUsers(searchText)
                    searchBar.setText(searchText)
                } else {
                    mainViewModel.setListUsers(null)
                    searchBar.clearText()
                }
                searchView.hide()
                true
            }

            searchBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favorit -> startActivity(
                        Intent(
                            this@MainActivity,
                            FavoriteActivity::class.java
                        )
                    )

                    R.id.settings -> startActivity(
                        Intent(
                            this@MainActivity,
                            SettingsActivity::class.java
                        )
                    )
                }
                true
            }

            val layoutManager = LinearLayoutManager(this@MainActivity)
            val decoration = DividerItemDecoration(this@MainActivity, layoutManager.orientation)
            recyclerView.layoutManager = layoutManager
            recyclerView.addItemDecoration(decoration)

            onBackPressedDispatcher.addCallback {
                if (searchView.isShowing) {
                    searchView.hide()
                    return@addCallback
                }
                if (searchBar.text.isNotEmpty()) {
                    searchBar.clearText()
                    mainViewModel.setListUsers(null)
                    return@addCallback
                }
                finish()
            }
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

    private fun setRecyclerView(usersList: ArrayList<ItemsItem>) {
        binding.recyclerView.adapter = RecyclerAdapterMain(usersList)
    }

    private fun displayProgressBar(isLoading: Boolean) {
        progressDialog = if (isLoading && progressDialog == null) {
            ProgressDialog(this).apply {
                setTitle("Memuat...")
                setMessage("Harap Tunggu...")
                setCancelable(false)
                show()
            }
        } else {
            progressDialog?.dismiss()
            null
        }
    }

    private fun displayAlertDialog(isFailedLoad: Boolean, context: Context) {
        if (isFailedLoad) {
            MaterialAlertDialogBuilder(context)
                .setTitle("No Connection")
                .setMessage("Failed to load data")
                .setPositiveButton("OK") { dialog, _ ->
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