package com.project.compose.feature.movie.viewmodel

import androidx.lifecycle.viewModelScope
import com.project.compose.core.common.base.BaseViewModel
import com.project.compose.core.common.utils.state.UiState
import com.project.compose.core.common.utils.state.UiState.StateInitial
import com.project.compose.core.common.utils.state.UiState.StateSuccess
import com.project.compose.core.data.model.local.MovieEntity
import com.project.compose.core.data.model.response.MovieDetailResponse
import com.project.compose.core.data.model.response.ReviewResponse
import com.project.compose.core.data.model.response.VideoResponse
import com.project.compose.core.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: AppRepository
) : BaseViewModel() {

    private val _movieDetails = MutableStateFlow<UiState<MovieDetailResponse>>(StateInitial)
    val movieDetails: StateFlow<UiState<MovieDetailResponse>> = _movieDetails.asStateFlow()

    private val _reviews = MutableStateFlow<UiState<List<ReviewResponse>>>(StateInitial)
    val reviews: StateFlow<UiState<List<ReviewResponse>>> = _reviews.asStateFlow()

    private val _videos = MutableStateFlow<UiState<List<VideoResponse>>>(StateInitial)
    val videos: StateFlow<UiState<List<VideoResponse>>> = _videos.asStateFlow()

    fun isFavorite(movieId: Int): StateFlow<Boolean> =
        repository.isFavorite(movieId).stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun fetchMovieDetails(movieId: Int) {
        collectApiAsUiState(
            response = repository.getMovieDetails(movieId),
            resetState = false,
            updateState = { _movieDetails.value = it }
        )
        collectApiAsUiState(
            response = repository.getMovieReviews(movieId).map { it.map { resp -> resp.results } },
            resetState = false,
            updateState = { _reviews.value = it }
        )
        collectApiAsUiState(
            response = repository.getMovieVideos(movieId).map { it.map { resp -> resp.results } },
            resetState = false,
            updateState = { _videos.value = it }
        )
    }

    fun toggleFavorite(isCurrentlyFavorite: Boolean) {
        val movieState = _movieDetails.value
        if (movieState is StateSuccess) {
            val movie = movieState.data ?: return
            viewModelScope.launch {
                if (isCurrentlyFavorite) {
                    repository.deleteMovie(movie.toEntity())
                } else {
                    repository.insertMovie(movie.toEntity())
                }
            }
        }
    }

    private fun MovieDetailResponse.toEntity() = MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate
    )
}
