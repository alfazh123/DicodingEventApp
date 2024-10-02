package com.example.submissionone.ui.home

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissionone.R
import com.example.submissionone.data.response.ListEventsItem

class ListEventAdapter(
    private val items: List<ListEventsItem>,
    private val isCarousel: Boolean
) : RecyclerView.Adapter<ListEventAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: ListEventsItem)
    }

    fun setOnClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: ListEventsItem, isCarousel: Boolean) {

            if(isCarousel) {
                val imageCoverCarousel = itemView.findViewById<ImageView>(R.id.imageView)
                val textTitleCarousel = itemView.findViewById<TextView>(R.id.textViewCarousel)
                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .into(imageCoverCarousel)
                textTitleCarousel.text = event.name
            } else {
                val imageCoverList = itemView.findViewById<ImageView>(R.id.img_item_photo)
                val textTitleList = itemView.findViewById<TextView>(R.id.tv_item_name)
                val textDescriptionList = itemView.findViewById<TextView>(R.id.tv_item_description)
                Glide.with(itemView.context)
                    .load(event.mediaCover)
                    .into(imageCoverList)
                textTitleList.text = event.name
                textDescriptionList.text = Html.fromHtml(event.description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = if (isCarousel) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.card_list_event, parent, false)
        }
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(items[position], isCarousel)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(items[position])
        }
    }
}