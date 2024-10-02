package com.example.submissionone.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissionone.data.response.DetailEventResponse
import com.example.submissionone.data.response.Event
import com.example.submissionone.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {

    private val _events = MutableLiveData<Event>()
    val events: MutableLiveData<Event> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: MutableLiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    var _id = 0

    fun setEventById(id: Int) {
        _id = id
    }

    init {
        findEventById(_id)
    }

    fun findEventById(id: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(id)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                _isSuccess.value = true
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _events.value = responseBody.event
                        Log.d("DetailEventViewModel", "onResponse: ${responseBody.event}")
                        _message.value = "Success ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.d("DetailEventViewModel", "onFailure: ${t.message}")
                _message.value = "Failed ${t.message}"
            }
        })
    }

}