package com.intermediate.storyapp.view.dashboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.intermediate.storyapp.data.response.ListStoryItem
import com.intermediate.storyapp.databinding.ItemStoryBinding
import com.intermediate.storyapp.view.story.StoryDetailActivity

class ItemAdapter(private val context: Context) :
    PagingDataAdapter<ListStoryItem, ItemAdapter.ViewHolder>(StoryComparator) {

    inner class ViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ListStoryItem?) {
            item?.let { listItem ->
                with(binding) {
                    username.text = listItem.name
                    descStory.text = listItem.description
                    Glide.with(root.context)
                        .load(listItem.photoUrl)
                        .into(imageUser)

                    root.setOnClickListener {
                        val intent = Intent(context, StoryDetailActivity::class.java)
                        intent.putExtra("item_id", listItem.id)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    object StoryComparator : DiffUtil.ItemCallback<ListStoryItem>() {
        override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
            return oldItem == newItem
        }
    }
}
