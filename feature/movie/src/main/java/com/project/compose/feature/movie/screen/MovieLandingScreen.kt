package com.project.compose.feature.movie.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.project.compose.core.common.R
import com.project.compose.core.common.base.BaseViewModel
import com.project.compose.core.common.ui.theme.NetflixRed
import com.project.compose.core.common.utils.onCustomClick
import com.project.compose.core.common.utils.state.UiState
import com.project.compose.core.data.model.response.MovieResponse
import com.project.compose.core.navigation.helper.Navigator
import com.project.compose.core.navigation.route.FavoriteGraph.FavoriteLandingRoute
import com.project.compose.core.navigation.route.HomeGraph.MovieDetailRoute
import com.project.compose.core.navigation.route.HomeGraph.MoviePlayerRoute
import com.project.compose.feature.movie.viewmodel.MovieLandingViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MovieLandingScreen(
    navigator: Navigator,
    viewModel: MovieLandingViewModel = hiltViewModel()
) {
    val popularMovies by viewModel.popularMovies.collectAsState()
    val topRatedMovies by viewModel.topRatedMovies.collectAsState()
    val nowPlayingMovies by viewModel.nowPlayingMovies.collectAsState()

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "moView",
                        color = NetflixRed,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { navigator.navigate(FavoriteLandingRoute) }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_true),
                            contentDescription = "My List",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
            ) {
                (popularMovies as? UiState.StateSuccess)?.data?.let { movies ->
                    val featuredMovies = remember(movies) {
                        movies.shuffled().take(5)
                    }
                    FeaturedPager(
                        movies = featuredMovies,
                        onPosterClick = { response ->
                            navigator.navigate(MovieDetailRoute(response.id))
                        },
                        onPlayClick = { response ->
                            navigator.navigate(MoviePlayerRoute(response.id))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                with(viewModel) {
                    MovieSection("Popular on Netflix", popularMovies) { movie ->
                        navigator.navigate(MovieDetailRoute(movie.id))
                    }
                    MovieSection("Top Rated", topRatedMovies) { movie ->
                        navigator.navigate(MovieDetailRoute(movie.id))
                    }
                    MovieSection("Now Playing", nowPlayingMovies) { movie ->
                        navigator.navigate(MovieDetailRoute(movie.id))
                    }
                }
            }
        }
    }
}

@Composable
private fun FeaturedPager(
    movies: List<MovieResponse>,
    onPosterClick: (MovieResponse) -> Unit,
    onPlayClick: (MovieResponse) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { movies.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(550.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val movie = movies[page]
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize().onCustomClick { onPosterClick(movie) },
                    model = "https://image.tmdb.org/t/p/original${movie.posterPath}",
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black,
                                    Color.Black.copy(alpha = 0.8f),
                                    Color.Black.copy(alpha = 0.6f),
                                    Color.Black
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.wrapContentWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(4.dp),
                        onClick = { onPlayClick(movie) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Play", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(movies.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.White else Color.Gray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .background(color, RoundedCornerShape(50))
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
private fun BaseViewModel.MovieSection(
    title: String,
    uiState: UiState<List<MovieResponse>>,
    onMovieClick: (MovieResponse) -> Unit
) {
    Column {
        if (uiState is UiState.StateSuccess) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            uiState.HandleUiState(
                onLoading = {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = NetflixRed
                    )
                },
                onSuccess = { movies ->
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(movies) { movie ->
                            MovieItem(movie, onMovieClick)
                        }
                    }
                },
                onFailed = { throwable ->
                    Text(
                        text = "Error: ${throwable.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun MovieItem(movie: MovieResponse, onMovieClick: (MovieResponse) -> Unit) {
    Card(
        modifier = Modifier
            .width(130.dp)
            .aspectRatio(2f / 3f)
            .clickable { onMovieClick(movie) },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
