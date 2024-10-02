package com.example.submissionone.data.response

data class DetailEventResponse(
	val error: Boolean,
	val message: String,
	val event: Event
)

data class Event(
	val summary: String,
	val mediaCover: String,
	val registrants: Int,
	val imageLogo: String,
	val link: String,
	val description: String,
	val ownerName: String,
	val cityName: String,
	val quota: Int,
	val name: String,
	val id: Int,
	val beginTime: String,
	val endTime: String,
	val category: String
)

