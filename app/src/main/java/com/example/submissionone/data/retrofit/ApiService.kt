package com.example.submissionone.data.retrofit

import retrofit2.Call
import com.example.submissionone.data.response.ActiveEventResponse
import com.example.submissionone.data.response.DetailEventResponse
//import com.example.submissionone.data.response.ListEventsItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("events?active=1")
    fun getEventUpcoming(): Call<ActiveEventResponse>

    @GET("events?active=0")
    fun getEventFinished(): Call<ActiveEventResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<DetailEventResponse>


//    @GET("events?active=-1&q={keyword}")
//    fun getEventSearch(
//        @Path("keyword") keyword: String
//    ): Call<List<ListEventsItem>>
}