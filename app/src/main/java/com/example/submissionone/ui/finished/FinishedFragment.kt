package com.example.submissionone.ui.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionone.data.response.ListEventsItem
import com.example.submissionone.databinding.FragmentFinishedBinding
import com.example.submissionone.ui.detail.DetailEventActivity

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val finishedViewModel =
            ViewModelProvider(this).get(FinishedViewModel::class.java)

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context)
        binding.rvFinished.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFinished.addItemDecoration(itemDecoration)

        binding.errorMessage.visibility = View.GONE

        finishedViewModel.isSuccess.observe(viewLifecycleOwner) { successFetch ->
            if (successFetch) {
                finishedViewModel.events.observe(viewLifecycleOwner) { events ->
                    setEvent(events)
                }
            } else {
                var messageFetch = ""
                finishedViewModel.message.observe(viewLifecycleOwner) { message ->
                    messageFetch = "Error Fetching Data: $message"
                }
                binding.errorMessage.visibility = View.VISIBLE
                Toast.makeText(context, messageFetch, Toast.LENGTH_LONG).show()
            }
        }
//        finishedViewModel.events.observe(viewLifecycleOwner) { events ->
//            setEvent(events)
//        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        return root
    }

    fun setEvent(event: List<ListEventsItem>) {
        val eventAdapter = EventFinishedAdapter()
        eventAdapter.submitList(event)
        binding.rvFinished.adapter = eventAdapter

        eventAdapter.setOnClickCallback(object : EventFinishedAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                val intent = Intent(context, DetailEventActivity::class.java)
                intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, data.id)
                startActivity(intent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}