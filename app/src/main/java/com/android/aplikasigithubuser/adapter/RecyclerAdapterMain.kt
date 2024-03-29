package com.android.aplikasigithubuser.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.aplikasigithubuser.databinding.ListUsersBinding
import com.android.aplikasigithubuser.response.ItemsItem
import com.android.aplikasigithubuser.ui.DetailUserActivity
import com.bumptech.glide.Glide

class RecyclerAdapterMain(private val githubUsers: ArrayList<ItemsItem>) :
    RecyclerView.Adapter<RecyclerAdapterMain.ViewHolder>() {

    class ViewHolder(private val binding: ListUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(users: ItemsItem) {
            with(binding) {
                Glide.with(root.context).load(users.avatarUrl).into(binding.circleImageView)
                textView.text = users.login
                materialButton.setOnClickListener {
                    it.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(users.htmlUrl)))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = githubUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = githubUsers[position]

        holder.bind(user)
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, DetailUserActivity::class.java).apply {
                putExtra("username", user.login)
                putExtra("avatar_url", user.avatarUrl)
                putExtra("htmlUrl", user.htmlUrl)
            }
            it.context.startActivity(intent)
        }
    }
}