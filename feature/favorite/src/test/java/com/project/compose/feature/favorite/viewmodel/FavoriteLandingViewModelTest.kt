package com.project.compose.feature.favorite.viewmodel

import com.project.compose.core.common.utils.state.UiState
import com.project.compose.core.data.model.local.MovieEntity
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
class FavoriteLandingViewModelTest {

    @Mock
    lateinit var repository: AppRepository

    private lateinit var viewModel: FavoriteLandingViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        val favoriteMovies = listOf(
            MovieEntity(1, "Title", "Overview", null, null, 8.0, "2024")
        )
        `when`(repository.getFavoriteMovies()).thenReturn(flowOf(favoriteMovies))
        
        viewModel = FavoriteLandingViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `favoriteMovies should emit StateSuccess when repository returns movies`() = runTest {
        val states = mutableListOf<UiState<List<MovieEntity>>>()
        val job = launch {
            viewModel.favoriteMovies.collect { states.add(it) }
        }
        
        advanceUntilIdle()
        
        val result = states.last()
        assert(result is UiState.StateSuccess)
        assertEquals(1, (result as UiState.StateSuccess).data?.size)
        assertEquals("Title", result.data?.first()?.title)
        
        job.cancel()
    }
}
