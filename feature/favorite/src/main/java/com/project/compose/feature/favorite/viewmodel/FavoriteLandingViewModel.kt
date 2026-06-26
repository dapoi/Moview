package com.project.compose.feature.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.compose.core.common.utils.state.UiState
import com.project.compose.core.common.utils.state.UiState.StateLoading
import com.project.compose.core.common.utils.state.UiState.StateSuccess
import com.project.compose.core.data.model.local.MovieEntity
import com.project.compose.core.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteLandingViewModel @Inject constructor(
    repository: AppRepository
) : ViewModel() {
    val favoriteMovies: StateFlow<UiState<List<MovieEntity>>> = repository.getFavoriteMovies()
        .map { movies -> StateSuccess(movies) as UiState<List<MovieEntity>> }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = StateLoading
        )
}
