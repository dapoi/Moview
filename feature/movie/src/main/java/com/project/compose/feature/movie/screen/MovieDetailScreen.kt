package com.project.compose.feature.movie.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.project.compose.core.common.R
import com.project.compose.core.common.ui.theme.Dimens.Dp16
import com.project.compose.core.common.utils.state.UiState.StateSuccess
import com.project.compose.core.data.model.response.ReviewResponse
import com.project.compose.core.navigation.helper.Navigator
import com.project.compose.core.navigation.route.HomeGraph.MovieDetailRoute
import com.project.compose.feature.movie.viewmodel.MovieDetailViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MovieDetailScreen(
    args: MovieDetailRoute,
    navigator: Navigator,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val movieState by viewModel.movieDetails.collectAsState()
    val reviewsState by viewModel.reviews.collectAsState()
    val isFavorite by viewModel.isFavorite(args.movieId).collectAsState()
    val context = LocalContext.current

    LaunchedEffect(args.movieId) {
        viewModel.fetchMovieDetails(args.movieId)
    }

    val movie = (movieState as? StateSuccess)?.data

    Scaffold(
        containerColor = Color.Black
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            with(viewModel) {
                movieState.HandleUiState(
                    onLoading = {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Red
                        )
                    },
                    onSuccess = { m ->
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(400.dp)
                                ) {
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/original${m.backdropPath}",
                                        contentDescription = m.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                brush = Brush.verticalGradient(
                                                    colors = listOf(
                                                        Color.Transparent,
                                                        Color.Black.copy(alpha = 0.7f),
                                                        Color.Black
                                                    )
                                                )
                                            )
                                    )
                                }
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = m.title,
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            m.releaseDate?.take(4) ?: "",
                                            color = Color.Gray,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Surface(
                                            color = Color.DarkGray,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                "13+",
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(
                                                    horizontal = 4.dp,
                                                    vertical = 2.dp
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color.Yellow,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            "%.1f".format(m.voteAverage),
                                            color = Color.Gray,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    androidx.compose.material3.Button(
                                        onClick = { navigator.navigate(com.project.compose.core.navigation.route.HomeGraph.MoviePlayerRoute(m.id)) },
                                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.White),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.PlayArrow, null, tint = Color.Black)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Play", color = Color.Black, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        m.overview,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White.copy(alpha = 0.8f),
                                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                                    )

                                    m.genres?.let { genres ->
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Genres: ${genres.joinToString { it.name }}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        "Reviews",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            item {
                                reviewsState.HandleUiState(
                                    onSuccess = { reviews ->
                                        if (reviews.isEmpty()) {
                                            Text(
                                                "No reviews available.",
                                                color = Color.Gray,
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        } else {
                                            Column {
                                                reviews.forEach { review ->
                                                    ReviewItem(review)
                                                }
                                            }
                                        }
                                    },
                                    onLoading = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(100.dp)
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.align(Alignment.Center),
                                                color = Color.Red
                                            )
                                        }
                                    },
                                    onFailed = {
                                        Text(
                                            text = "Failed to load reviews",
                                            color = Color.Gray,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                )
                            }
                        }
                    },
                    onFailed = { throwable ->
                        Text(
                            text = "Error: ${throwable.message}",
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dp16)
                    .align(Alignment.TopCenter)
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navigator.goBack() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Row {
                    IconButton(
                        onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Check out this movie: ${movie?.title}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black.copy(alpha = 0.5f)
                        )
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_share),
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { viewModel.toggleFavorite(isFavorite) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black.copy(alpha = 0.5f)
                        )
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isFavorite) R.drawable.ic_favorite_true
                                else R.drawable.ic_favorite_false
                            ),
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewItem(review: ReviewResponse) {
    Surface(
        color = Color(0xFF1A1A1A),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                review.author,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                review.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
