package com.android.aplikasigithubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.aplikasigithubuser.R
import com.android.aplikasigithubuser.adapter.RecyclerAdapterFollow
import com.android.aplikasigithubuser.databinding.FragmentFollowerBinding
import com.android.aplikasigithubuser.response.ResponseFollowItem
import com.android.aplikasigithubuser.viewmodel.MainViewModel

class FollowerFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: FragmentFollowerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        val decoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(decoration)
        val data = arguments?.getString("username")

        with(mainViewModel) {
            setListFollowers(data!!)
            getListFollowers.observe(viewLifecycleOwner) {
                setRecyclerView(it)

                emptyState(it.isEmpty())
            }

            isLoading.observe(viewLifecycleOwner) {
                displayProgressBar(it)
            }
        }
    }

    private fun setRecyclerView(usersList: ArrayList<ResponseFollowItem>) {
        binding.recyclerView.adapter = RecyclerAdapterFollow(usersList)
    }

    private fun displayProgressBar(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun emptyState(isEmpty: Boolean) {
        val showVisibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.included.emptyTitle.visibility = showVisibility
        binding.included.emptyText.apply {
            visibility = showVisibility
            text = getString(R.string.followers_empty)
        }
    }
}