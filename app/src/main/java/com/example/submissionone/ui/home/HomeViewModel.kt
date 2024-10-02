package com.example.submissionone.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionone.data.response.ActiveEventResponse
import com.example.submissionone.data.response.ListEventsItem
import com.example.submissionone.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    
    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent
    
    private val _finishedEvent = MutableLiveData<List<ListEventsItem>>()
    val finishedEvent: LiveData<List<ListEventsItem>> = _finishedEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccessUpcoming = MutableLiveData<Boolean>()
    val isSuccessUpcoming: LiveData<Boolean> = _isSuccessUpcoming

    private val _isSuccessFinished = MutableLiveData<Boolean>()
    val isSuccessFinished: LiveData<Boolean> = _isSuccessFinished

    private val _isCarousel = MutableLiveData<Boolean>()
    val isCarousel: LiveData<Boolean> = _isCarousel

    init {
        getUpcomingEvent()
        getFinishedEvent()
    }
    
    fun getUpcomingEvent() {
        _isLoading.value = true
//        val client = ApiConfig.getApiService().getEventUpcoming()
        val client = if (_isCarousel.value == true) {
            ApiConfig.getApiService().getEventUpcoming()
        } else {
            ApiConfig.getApiService().getEventUpcoming()
        }
        client.enqueue(object : Callback<ActiveEventResponse> {
            override fun onResponse(
                call: Call<ActiveEventResponse>,
                response: Response<ActiveEventResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _upcomingEvent.value = responseBody.listEvents
                        _isSuccessUpcoming.value = true
                    } else {
                        Log.d("HomeViewModel", "onFailure: ${response.message()}")
                        _isSuccessUpcoming.value = false
                    }
                }
            }

            override fun onFailure(call: Call<ActiveEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("HomeViewModel", "onFailure: ${t.message}")
                _isSuccessUpcoming.value = false
            }

        })
    }

    fun getFinishedEvent() {
        val client = ApiConfig.getApiService().getEventFinished()
        client.enqueue(object : Callback<ActiveEventResponse> {
            override fun onResponse(
                call: Call<ActiveEventResponse>,
                response: Response<ActiveEventResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _finishedEvent.value = responseBody.listEvents
                        _isSuccessFinished.value = true
                    } else {
                        Log.d("HomeViewModel", "onFailure: ${response.message()}")
                        _isSuccessFinished.value = false
                    }
                }
            }

            override fun onFailure(call: Call<ActiveEventResponse>, t: Throwable) {
                Log.e("HomeViewModel", "onFailure: ${t.message}")
                _isSuccessFinished.value = false
            }

        })
    }
    
}