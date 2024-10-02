package com.example.submissionone.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submissionone.data.response.ListEventsItem
import com.example.submissionone.databinding.FragmentHomeBinding
import com.example.submissionone.ui.detail.DetailEventActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManagerUpcoming = LinearLayoutManager(context)
        binding.rvUpcomingPreview.layoutManager = layoutManagerUpcoming
        val itemDecoration = DividerItemDecoration(context, layoutManagerUpcoming.orientation)
        binding.rvUpcomingPreview.addItemDecoration(itemDecoration)

        val layoutManagerFinished = LinearLayoutManager(context)
        binding.rvFinishedPreview.layoutManager = layoutManagerFinished
        val itemDecorationFinished = DividerItemDecoration(context, layoutManagerFinished.orientation)
        binding.rvFinishedPreview.addItemDecoration(itemDecorationFinished)

//        searchEvent = binding.searchView


        binding.errorMessageUpcomingEvetns.visibility = View.GONE
        binding.errorMessageFinishedEvents.visibility = View.GONE


        homeViewModel.finishedEvent.observe(viewLifecycleOwner) { events ->
            if (events.size > 5) {
                binding.errorMessageFinishedEvents.visibility = View.GONE
                val list = events.subList(0, 5)
                setListOrCardEvent(list, false)
            } else {
                setListOrCardEvent(events, false)
            }
        }

        homeViewModel.upcomingEvent.observe(viewLifecycleOwner) { events ->
            if (events.size > 5) {
                binding.errorMessageUpcomingEvetns.visibility = View.GONE
                val carousel = events.subList(0, 5)
                setListOrCardEvent(carousel, true)
            } else {
                setListOrCardEvent(events, true)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            setLoadState(it)
        }

        return root
    }

    fun setListOrCardEvent(list: List<ListEventsItem>, isCarousel: Boolean) {

        if (isCarousel) {
            val carouselAdapter = ListEventAdapter(list, true)
            binding.rvUpcomingPreview.adapter = carouselAdapter
            binding.rvUpcomingPreview.layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            carouselAdapter.setOnClickCallback(object : ListEventAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListEventsItem) {
                    val intent = Intent(context, DetailEventActivity::class.java)
                    intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, data.id)
                    startActivity(intent)
                }
            })
        } else {
            val listEventAdapter = ListEventAdapter(list, false)
            binding.rvFinishedPreview.adapter = listEventAdapter

            listEventAdapter.setOnClickCallback(object : ListEventAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListEventsItem) {
                    val intent = Intent(context, DetailEventActivity::class.java)
                    intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, data.id)
                    startActivity(intent)
                }
            })
        }
    }

    fun setLoadState(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}