package com.example.submissionone.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionone.data.response.ListEventsItem
import com.example.submissionone.databinding.FragmentUpcomingBinding
import com.example.submissionone.ui.detail.DetailEventActivity

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    private val upcomingEventViewModel by viewModels<UpcomingViewModel>()
//    private lateinit var adapter: EventUpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val upcomingViewModel = ViewModelProvider(this).get(UpcomingViewModel::class.java)

        val layoutManager = LinearLayoutManager(context)
        binding.rvUpcoming.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvUpcoming.addItemDecoration(itemDecoration)

        upcomingViewModel.events.observe(viewLifecycleOwner) { events ->
            setEvent(events)
        }
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        return root
    }

    private fun setEvent(event: List<ListEventsItem>) {
        val eventAdapter = EventUpcomingAdapter()
        eventAdapter.submitList(event)
        binding.rvUpcoming.adapter = eventAdapter

        eventAdapter.setOnClickCallback(object : EventUpcomingAdapter.OnItemClickback {
            override fun onItemClicked(data: ListEventsItem) {
                val moveIntent = Intent(context, DetailEventActivity::class.java)
                moveIntent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, data.id)
                startActivity(moveIntent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
