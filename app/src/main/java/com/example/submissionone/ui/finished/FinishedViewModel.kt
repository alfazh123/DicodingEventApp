package com.example.submissionone.ui.finished

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

class FinishedViewModel : ViewModel() {

    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    companion object {
        private const val TAG = "FinishedViewModel"
    }

    init {
        getFinishedEvent()
    }

    private fun getFinishedEvent() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventFinished()
        client.enqueue(object : Callback<ActiveEventResponse> {
            override fun onResponse(
                call: Call<ActiveEventResponse>,
                response: Response<ActiveEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _events.value = responseBody.listEvents
                        _isSuccess.value = true
                    } else {
                        Log.d(TAG, "onFailure: ${response.message()}")
                        _isSuccess.value = false
                        _message.value = response.message()
                    }
                }
            }

            override fun onFailure(call: Call<ActiveEventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _isSuccess.value = false
                _message.value = t.message
            }
        })
    }

}
