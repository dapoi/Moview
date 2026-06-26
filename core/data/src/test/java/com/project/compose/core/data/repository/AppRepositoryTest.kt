package com.project.compose.core.data.repository

import com.project.compose.core.common.utils.state.ApiState
import com.project.compose.core.data.model.local.MovieEntity
import com.project.compose.core.data.model.response.MovieDetailResponse
import com.project.compose.core.data.model.response.MovieListResponse
import com.project.compose.core.data.source.local.MovieDao
import com.project.compose.core.data.source.remote.ApiService
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AppRepositoryTest {

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var movieDao: MovieDao

    private lateinit var repository: AppRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = AppRepositoryImpl(apiService, movieDao)
    }

    @Test
    fun `getPopularMovies should call apiService and emit Loading then Success`() = runTest {
        val expectedResponse = MovieListResponse(emptyList())
        `when`(apiService.getPopularMovies()).thenReturn(expectedResponse)

        val results = repository.getPopularMovies().toList()

        assertEquals(2, results.size)
        assertTrue(results[0] is ApiState.Loading)
        assertEquals(ApiState.Success(expectedResponse), results[1])
        verify(apiService).getPopularMovies()
    }

    @Test
    fun `getMovieDetails should call apiService and emit Loading then Success`() = runTest {
        val movieId = 1
        val expectedResponse = MovieDetailResponse(
            id = movieId,
            title = "Test",
            overview = "Desc",
            posterPath = null,
            backdropPath = null,
            voteAverage = 8.0,
            releaseDate = "2024",
            genres = emptyList()
        )
        `when`(apiService.getMovieDetails(movieId)).thenReturn(expectedResponse)

        val results = repository.getMovieDetails(movieId).toList()

        assertEquals(2, results.size)
        assertTrue(results[0] is ApiState.Loading)
        assertEquals(ApiState.Success(expectedResponse), results[1])
        verify(apiService).getMovieDetails(movieId)
    }

    @Test
    fun `getFavoriteMovies should call movieDao`() {
        val expectedFlow = flowOf(emptyList<MovieEntity>())
        `when`(movieDao.getFavoriteMovies()).thenReturn(expectedFlow)

        val result = repository.getFavoriteMovies()

        assertEquals(expectedFlow, result)
        verify(movieDao).getFavoriteMovies()
    }
}
