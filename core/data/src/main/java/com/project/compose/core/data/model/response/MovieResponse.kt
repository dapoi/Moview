package com.project.compose.core.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieListResponse(
    @Json(name = "results") val results: List<MovieResponse>
)

@JsonClass(generateAdapter = true)
data class MovieResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "release_date") val releaseDate: String?
)

@JsonClass(generateAdapter = true)
data class MovieDetailResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "genres") val genres: List<GenreResponse>?
)

@JsonClass(generateAdapter = true)
data class GenreResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class ReviewListResponse(
    @Json(name = "results") val results: List<ReviewResponse>
)

@JsonClass(generateAdapter = true)
data class ReviewResponse(
    @Json(name = "id") val id: String,
    @Json(name = "author") val author: String,
    @Json(name = "content") val content: String
)
