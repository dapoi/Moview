package com.project.compose.core.data.repository

import com.project.compose.core.common.utils.state.ApiState
import com.project.compose.core.data.model.local.MovieEntity
import com.project.compose.core.data.model.response.MovieDetailResponse
import com.project.compose.core.data.model.response.MovieListResponse
import com.project.compose.core.data.model.response.ReviewListResponse
import com.project.compose.core.data.model.response.VideoListResponse
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getPopularMovies(): Flow<ApiState<MovieListResponse>>
    fun getTopRatedMovies(): Flow<ApiState<MovieListResponse>>
    fun getNowPlayingMovies(): Flow<ApiState<MovieListResponse>>
    fun getMovieDetails(movieId: Int): Flow<ApiState<MovieDetailResponse>>
    fun getMovieReviews(movieId: Int): Flow<ApiState<ReviewListResponse>>
    fun getMovieVideos(movieId: Int): Flow<ApiState<VideoListResponse>>

    fun getFavoriteMovies(): Flow<List<MovieEntity>>
    fun isFavorite(id: Int): Flow<Boolean>
    suspend fun insertMovie(movie: MovieEntity)
    suspend fun deleteMovie(movie: MovieEntity)
}
