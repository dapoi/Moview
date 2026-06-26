package com.project.compose.core.data.source.remote

import com.project.compose.core.data.model.response.MovieDetailResponse
import com.project.compose.core.data.model.response.MovieListResponse
import com.project.compose.core.data.model.response.ReviewListResponse
import com.project.compose.core.data.model.response.VideoListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "4f948dc2d121184b17586b04a38b778a"
    ): MovieListResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = "4f948dc2d121184b17586b04a38b778a"
    ): MovieListResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = "4f948dc2d121184b17586b04a38b778a"
    ): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = "4f948dc2d121184b17586b04a38b778a"
    ): MovieDetailResponse

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = "4f948dc2d121184b17586b04a38b778a"
    ): ReviewListResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = "4f948dc2d121184b17586b04a38b778a"
    ): VideoListResponse
}
