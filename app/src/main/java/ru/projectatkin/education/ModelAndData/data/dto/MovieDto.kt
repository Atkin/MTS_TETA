package ru.projectatkin.education.ModelAndData.data.dto

data class MovieDto(
	var title: String?,
	var description: String?,
	var rateScore: Int?,
	var ageRestriction: String?,
	var imageUrl: String?,
	var genre: String?,
	var date: String?
)
