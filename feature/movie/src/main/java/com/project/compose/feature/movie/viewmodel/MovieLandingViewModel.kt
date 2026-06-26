package com.project.compose.feature.movie.viewmodel

import com.project.compose.core.common.base.BaseViewModel
import com.project.compose.core.common.utils.state.UiState
import com.project.compose.core.common.utils.state.UiState.StateInitial
import com.project.compose.core.data.model.response.MovieResponse
import com.project.compose.core.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MovieLandingViewModel @Inject constructor(
    private val repository: AppRepository
) : BaseViewModel() {

    private val _popularMovies = MutableStateFlow<UiState<List<MovieResponse>>>(StateInitial)
    val popularMovies: StateFlow<UiState<List<MovieResponse>>> = _popularMovies.asStateFlow()

    private val _topRatedMovies = MutableStateFlow<UiState<List<MovieResponse>>>(StateInitial)
    val topRatedMovies: StateFlow<UiState<List<MovieResponse>>> = _topRatedMovies.asStateFlow()

    private val _nowPlayingMovies = MutableStateFlow<UiState<List<MovieResponse>>>(StateInitial)
    val nowPlayingMovies: StateFlow<UiState<List<MovieResponse>>> = _nowPlayingMovies.asStateFlow()

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        collectApiAsUiState(
            response = repository.getPopularMovies().map { it.map { resp -> resp.results } },
            resetState = false,
            updateState = { _popularMovies.value = it }
        )
        collectApiAsUiState(
            response = repository.getTopRatedMovies().map { it.map { resp -> resp.results } },
            resetState = false,
            updateState = { _topRatedMovies.value = it }
        )
        collectApiAsUiState(
            response = repository.getNowPlayingMovies().map { it.map { resp -> resp.results } },
            resetState = false,
            updateState = { _nowPlayingMovies.value = it }
        )
    }
}
