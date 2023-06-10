package com.example.storyappsubmission.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappsubmission.data.model.Story
import com.example.storyappsubmission.databinding.ItemStoryBinding
import com.example.storyappsubmission.ui.detailstory.DetailActivity

class StoryAdapter: PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : Story) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(ivStory)
                tvUsername.text = item.name
                tvDescription.text = item.description

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_USERNAME, item.id)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivStory, "picture"),
                            Pair(tvUsername, "username"),
                            Pair(tvDescription, "description")
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder , position : Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Story> =
            object : DiffUtil.ItemCallback<Story>() {
                override fun areItemsTheSame(oldUser: Story, newUser: Story): Boolean {
                    return oldUser == newUser
                }

                override fun areContentsTheSame(oldUser: Story, newUser: Story): Boolean {
                    return oldUser.id == newUser.id
                }
            }
    }
}