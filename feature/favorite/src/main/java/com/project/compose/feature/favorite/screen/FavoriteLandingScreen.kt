package com.project.compose.feature.favorite.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.project.compose.core.common.R
import com.project.compose.core.common.ui.theme.NetflixRed
import com.project.compose.core.common.utils.state.UiState.StateFailed
import com.project.compose.core.common.utils.state.UiState.StateLoading
import com.project.compose.core.common.utils.state.UiState.StateSuccess
import com.project.compose.core.data.model.local.MovieEntity
import com.project.compose.core.navigation.helper.Navigator
import com.project.compose.core.navigation.route.HomeGraph.MovieDetailRoute
import com.project.compose.feature.favorite.viewmodel.FavoriteLandingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoriteLandingScreen(
    navigator: Navigator,
    viewModel: FavoriteLandingViewModel = hiltViewModel()
) {
    val uiState by viewModel.favoriteMovies.collectAsState()

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My List",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.goBack() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when (uiState) {
                is StateLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = NetflixRed
                    )
                }

                is StateSuccess -> {
                    val movies = (uiState as StateSuccess).data ?: emptyList()
                    if (movies.isEmpty()) {
                        Text(
                            text = "You haven't added anything to your list yet.",
                            color = Color.Gray,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(movies) { movie ->
                                FavoriteMovieItem(movie) {
                                    navigator.navigate(MovieDetailRoute(movie.id))
                                }
                            }
                        }
                    }
                }

                is StateFailed -> {
                    Text(
                        text = "Error: ${(uiState as StateFailed).throwable.message}",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {}
            }
        }
    }
}

@Composable
private fun FavoriteMovieItem(movie: MovieEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Card(
            modifier = Modifier.size(width = 100.dp, height = 150.dp),
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

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = movie.releaseDate?.take(4) ?: "N/A",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "%.1f".format(movie.voteAverage),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
