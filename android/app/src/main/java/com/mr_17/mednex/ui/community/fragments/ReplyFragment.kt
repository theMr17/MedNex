package com.mr_17.mednex.ui.community.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mr_17.mednex.R
import com.mr_17.mednex.data.Resource
import com.mr_17.mednex.databinding.FragmentReplyBinding
import com.mr_17.mednex.ui.community.CommunityViewModel
import com.mr_17.mednex.ui.community.adapters.PostsRecyclerViewAdapter
import com.mr_17.mednex.ui.community.models.Post
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReplyFragment : Fragment(R.layout.fragment_reply), PostsRecyclerViewAdapter.OnClickListener {
    private lateinit var binding: FragmentReplyBinding
    private val communityViewModel by viewModels<CommunityViewModel>()
    private val args by navArgs<ReplyFragmentArgs>()
    private lateinit var post: Post

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReplyBinding.bind(view)

        post = args.post

        communityViewModel.getAllChildrenPosts(post.childrenIdList!!)

        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            communityViewModel.allChildrenPostsFlow.collectLatest {
                when(it) {
                    is Resource.Error -> {
                        showLoading(false)
                        showToast(it.message.toString())
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        binding.apply {
                            post.apply {
                                tvMessage.text = this@ReplyFragment.post.text
                                ibReply.isVisible = false
                                ibLike.isVisible = false
                            }

                            rvPostsReplies.apply {
                                adapter = PostsRecyclerViewAdapter(
                                    it.data!!,
                                    context,
                                    this@ReplyFragment
                                )
                                layoutManager = LinearLayoutManager(context)
                            }
                        }
                        showToast(it.data.toString())
                    }
                    is Resource.Loading -> {
                        showLoading(true)
                        showToast("loading")
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            circularProgressIndicator.isVisible = isLoading
            rvPostsReplies.isVisible = !isLoading
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLikeButtonClick(v: View?, post: Post) {

    }

    override fun onReplyButtonClick(v: View?, post: Post) {

    }
}