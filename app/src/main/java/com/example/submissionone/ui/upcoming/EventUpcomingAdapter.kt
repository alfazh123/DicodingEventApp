package com.example.submissionone.ui.upcoming

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionone.R
import com.example.submissionone.ui.upcoming.EventUpcomingAdapter.*
import com.example.submissionone.data.response.ListEventsItem
import com.example.submissionone.databinding.CardEventBinding

class EventUpcomingAdapter : ListAdapter<ListEventsItem, EventViewHolder>(DIFF_CALLBACK) {

    private lateinit var onEventClickCallback: OnItemClickback

    interface OnItemClickback {
        fun onItemClicked(data: ListEventsItem)
    }

    fun setOnClickCallback(onItemClickback: OnItemClickback) {
        this.onEventClickCallback = onItemClickback
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = CardEventBinding.bind(itemView)
        fun bind(event: ListEventsItem){
            binding.tvItemName.text = event.name
            val description = Html.fromHtml(event.description)
            binding.tvItemDescription.text = description
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(binding.imgItemPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = LayoutInflater.from(parent.context).inflate(R.layout.card_event, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review)

        holder.itemView.setOnClickListener {
            onEventClickCallback.onItemClicked(getItem(holder.adapterPosition))
        }
    }
}