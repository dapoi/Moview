package com.project.compose.core.data.model.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VideoListResponse(
    @Json(name = "results") val results: List<VideoResponse>
)

@JsonClass(generateAdapter = true)
data class VideoResponse(
    @Json(name = "id") val id: String,
    @Json(name = "key") val key: String,
    @Json(name = "name") val name: String,
    @Json(name = "site") val site: String,
    @Json(name = "type") val type: String
)
