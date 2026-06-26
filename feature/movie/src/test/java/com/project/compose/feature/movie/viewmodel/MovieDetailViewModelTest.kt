package com.project.compose.feature.movie.viewmodel

import com.project.compose.core.common.utils.state.ApiState
import com.project.compose.core.common.utils.state.UiState
import com.project.compose.core.data.model.response.MovieDetailResponse
import com.project.compose.core.data.model.response.ReviewListResponse
import com.project.compose.core.data.model.response.ReviewResponse
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
class MovieDetailViewModelTest {

    @Mock
    lateinit var repository: AppRepository

    private lateinit var viewModel: MovieDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        
        viewModel = MovieDetailViewModel(repository)
        viewModel.dispatcher = testDispatcher
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMovieDetails should emit movie details and reviews`() = runTest {
        val movieId = 1
        val movieDetail = MovieDetailResponse(movieId, "Title", "Overview", null, null, 8.0, "2024", emptyList())
        val reviews = ReviewListResponse(listOf(ReviewResponse("1", "Author", "Content")))
        
        `when`(repository.getMovieDetails(movieId)).thenReturn(flowOf(ApiState.Success(movieDetail)))
        `when`(repository.getMovieReviews(movieId)).thenReturn(flowOf(ApiState.Success(reviews)))
        
        viewModel.fetchMovieDetails(movieId)
        advanceUntilIdle()
        
        assert(viewModel.movieDetails.value is UiState.StateSuccess)
        assertEquals("Title", (viewModel.movieDetails.value as UiState.StateSuccess).data?.title)
        
        assert(viewModel.reviews.value is UiState.StateSuccess)
        assertEquals(1, (viewModel.reviews.value as UiState.StateSuccess).data?.size)
    }

    @Test
    fun `isFavorite should return flow from repository`() = runTest {
        val movieId = 1
        `when`(repository.isFavorite(movieId)).thenReturn(flowOf(true))
        
        val result = mutableListOf<Boolean>()
        val job = launch {
            viewModel.isFavorite(movieId).collect { result.add(it) }
        }
        
        advanceUntilIdle()
        
        assertEquals(true, result.last())
        job.cancel()
    }
}
