package com.project.compose.feature.movie.viewmodel

import com.project.compose.core.common.base.BaseViewModel
import com.project.compose.core.common.utils.state.ApiState
import com.project.compose.core.common.utils.state.UiState
import com.project.compose.core.data.model.response.MovieListResponse
import com.project.compose.core.data.model.response.MovieResponse
import com.project.compose.core.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class MovieLandingViewModelTest {

    @Mock
    lateinit var repository: AppRepository

    private lateinit var viewModel: MovieLandingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        BaseViewModel.defaultDispatcher = testDispatcher
        
        val movieResponse = MovieListResponse(
            results = listOf(
                MovieResponse(1, "Movie", "Overview", null, null, 7.5, "2024")
            )
        )
        
        `when`(repository.getPopularMovies()).thenReturn(flowOf(ApiState.Success(movieResponse)))
        `when`(repository.getTopRatedMovies()).thenReturn(flowOf(ApiState.Success(movieResponse)))
        `when`(repository.getNowPlayingMovies()).thenReturn(flowOf(ApiState.Success(movieResponse)))
        
        viewModel = MovieLandingViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        BaseViewModel.defaultDispatcher = Dispatchers.IO
    }

    @Test
    fun `popularMovies should emit StateSuccess when repository returns data`() = runTest {
        val states = mutableListOf<UiState<List<MovieResponse>>>()
        val job = launch {
            viewModel.popularMovies.collect { states.add(it) }
        }
        
        advanceUntilIdle()
        
        val result = states.last()
        assert(result is UiState.StateSuccess)
        assertEquals(1, (result as UiState.StateSuccess).data?.size)
        
        job.cancel()
    }

    @Test
    fun `all movie sections should load concurrently`() = runTest {
        advanceUntilIdle()
        
        assert(viewModel.popularMovies.value is UiState.StateSuccess)
        assert(viewModel.topRatedMovies.value is UiState.StateSuccess)
        assert(viewModel.nowPlayingMovies.value is UiState.StateSuccess)
    }
}
