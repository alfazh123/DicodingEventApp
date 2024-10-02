package com.example.submissionone.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.submissionone.R
import com.example.submissionone.data.response.Event
import com.example.submissionone.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

//    private lateinit var binding: ActivityDetailEventBinding

    private lateinit var imgCover: ImageView
    private lateinit var imgLogo: ImageView
    private lateinit var ownerName: TextView
    private lateinit var eventName: TextView
    private lateinit var descriptionEvent: TextView
    private lateinit var startDateEvent: TextView
    private lateinit var cityName: TextView
    private lateinit var quota: TextView
    private lateinit var category: TextView
    private lateinit var link: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRegister: Button

    private lateinit var detailEventViewModel: DetailEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val eventId = if(Build.VERSION.SDK_INT >= 34) {
            intent.getIntExtra(EXTRA_EVENT_ID, 0)
        } else {
            intent.getIntExtra(EXTRA_EVENT_ID,0)
        }

        detailEventViewModel = ViewModelProvider(this).get(DetailEventViewModel::class.java)
        detailEventViewModel.setEventById(eventId)

        imgCover = binding.imgCover
        imgLogo = binding.imgLogo
        ownerName = binding.tvOwnerName
        eventName = binding.tvEventName
        descriptionEvent = binding.descriptionEvent
        startDateEvent = binding.startDateEvent
        cityName = binding.cityName
        quota = binding.quota
        category = binding.category
        link = binding.link
        progressBar = binding.progressBar
        btnRegister = binding.btnRegister

        binding.errorMessageDetailEvent.visibility = View.GONE


//        if (eventId != null) {

            detailEventViewModel.isSuccess.observe(this) { successFetch ->
                if (successFetch) {
                    detailEventViewModel.events.observe(this) { eventsResponse ->
                        setEvent(eventsResponse)
                    }
                } else {
                    var messageFetch = ""
                    detailEventViewModel.message.observe(this) { message ->
                        messageFetch = "Error Fetching Data: $message"
                    }
                    binding.errorMessageDetailEvent.visibility = View.VISIBLE
                    Log.d("DetailEventActivity", messageFetch)
                }
//            }

            binding.errorMessageDetailEvent.visibility = View.GONE
//            findEventById(eventId)
            detailEventViewModel.events.observe(this) { eventsResponse ->
                setEvent(eventsResponse)
            }

            Toast.makeText(this, "Event ID: $eventId", Toast.LENGTH_SHORT).show()

            detailEventViewModel.findEventById(eventId)

            detailEventViewModel.isLoading.observe(this) { isLoading ->
                setLoadState(isLoading)
            }
        }
    }

    fun setEvent(eventsResponse: Event) {
        progressBar.visibility = View.INVISIBLE
        Glide.with(this@DetailEventActivity)
            .load(eventsResponse.mediaCover)
            .into(imgCover)
        Glide.with(this@DetailEventActivity)
            .load(eventsResponse.imageLogo)
            .into(imgLogo)
        ownerName.text = getString(R.string.owner_event, eventsResponse.ownerName)
        eventName.text = eventsResponse.name
        val description = Html.fromHtml(eventsResponse.description)
        descriptionEvent.text = description
        startDateEvent.text = getString(R.string.start_date, eventsResponse.beginTime)
//        endDateEvent.text = getString(R.string.end_date, eventsResponse.event.endTime)
        cityName.text = getString(R.string.location, eventsResponse.cityName)
        val sisaOrang = eventsResponse.quota - eventsResponse.registrants
        quota.text = getString(R.string.sisa_quota,sisaOrang.toString())
        category.text = getString(R.string.category, eventsResponse.category)
        link.text = eventsResponse.link
        btnRegister.setOnClickListener {
            openBrowser(eventsResponse.link)
        }
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun setLoadState(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}