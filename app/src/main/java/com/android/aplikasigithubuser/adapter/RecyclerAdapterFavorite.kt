package com.android.aplikasigithubuser.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.aplikasigithubuser.database.Favorite
import com.android.aplikasigithubuser.database.FavoriteDatabase
import com.android.aplikasigithubuser.databinding.ListFavoriteBinding
import com.android.aplikasigithubuser.ui.DetailUserActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerAdapterFavorite() :
    RecyclerView.Adapter<RecyclerAdapterFavorite.ViewHolder>() {

    private val listFavorite = ArrayList<Favorite>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Favorite>) {
        listFavorite.clear()
        listFavorite.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val favoriteDao by lazy {
            FavoriteDatabase.database(itemView.context).favoriteDao()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteFavorite(username: String) {
            CoroutineScope(Dispatchers.IO).launch {
                favoriteDao.deleteData(username)
                withContext(Dispatchers.Main) {
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(itemPosition: Favorite) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(Uri.parse(itemPosition.avatarUrl))
                    .into(circleImageView)
                textView.text = itemPosition.username
                openLinkButton.setOnClickListener {
                    itemView.context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(itemPosition.htmlUrl)
                        )
                    )
                }
                favoriteButton.isChecked = true
                favoriteButton.addOnCheckedChangeListener { button, bool ->
                    button.isChecked = bool
                    deleteFavorite(itemPosition.username)
                }
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding = ListFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemPosition = listFavorite[position]

        holder.bind(itemPosition)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailUserActivity::class.java).apply {
                putExtra("username", itemPosition.username)
                putExtra("avatar_url", itemPosition.avatarUrl)
                putExtra("htmlUrl", itemPosition.htmlUrl)
            }
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listFavorite.size
}