package com.project.compose.core.data.repository

import com.project.compose.core.common.base.BaseRepository
import com.project.compose.core.common.utils.state.ApiState
import com.project.compose.core.data.model.local.MovieEntity
import com.project.compose.core.data.model.response.MovieDetailResponse
import com.project.compose.core.data.model.response.MovieListResponse
import com.project.compose.core.data.model.response.ReviewListResponse
import com.project.compose.core.data.model.response.VideoListResponse
import com.project.compose.core.data.source.local.MovieDao
import com.project.compose.core.data.source.remote.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao
) : BaseRepository(), AppRepository {
    override fun getPopularMovies(): Flow<ApiState<MovieListResponse>> = collectApiResult(
        fetchApi = { apiService.getPopularMovies() },
        transformData = { it }
    )

    override fun getTopRatedMovies(): Flow<ApiState<MovieListResponse>> = collectApiResult(
        fetchApi = { apiService.getTopRatedMovies() },
        transformData = { it }
    )

    override fun getNowPlayingMovies(): Flow<ApiState<MovieListResponse>> = collectApiResult(
        fetchApi = { apiService.getNowPlayingMovies() },
        transformData = { it }
    )

    override fun getMovieDetails(movieId: Int): Flow<ApiState<MovieDetailResponse>> = collectApiResult(
        fetchApi = { apiService.getMovieDetails(movieId) },
        transformData = { it }
    )

    override fun getMovieReviews(movieId: Int): Flow<ApiState<ReviewListResponse>> = collectApiResult(
        fetchApi = { apiService.getMovieReviews(movieId) },
        transformData = { it }
    )

    override fun getMovieVideos(movieId: Int): Flow<ApiState<VideoListResponse>> = collectApiResult(
        fetchApi = { apiService.getMovieVideos(movieId) },
        transformData = { it }
    )

    override fun getFavoriteMovies(): Flow<List<MovieEntity>> = movieDao.getFavoriteMovies()

    override fun isFavorite(id: Int): Flow<Boolean> = movieDao.isFavorite(id)

    override suspend fun insertMovie(movie: MovieEntity) = movieDao.insertMovie(movie)

    override suspend fun deleteMovie(movie: MovieEntity) = movieDao.deleteMovie(movie)
}
