package com.project.compose.feature.movie.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.project.compose.core.common.utils.state.UiState
import com.project.compose.core.navigation.helper.Navigator
import com.project.compose.core.navigation.route.HomeGraph.MoviePlayerRoute
import com.project.compose.feature.movie.viewmodel.MovieDetailViewModel

@Composable
internal fun MoviePlayerScreen(
    args: MoviePlayerRoute,
    navigator: Navigator,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val videosState by viewModel.videos.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(args.movieId) {
        viewModel.fetchMovieDetails(args.movieId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (val state = videosState) {
            is UiState.StateLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Red
                )
            }
            is UiState.StateSuccess -> {
                val trailer = state.data?.firstOrNull { 
                    it.site.lowercase() == "youtube" && 
                    (it.type.lowercase() == "trailer" || it.type.lowercase() == "teaser")
                }
                
                if (trailer != null) {
                    AndroidView(
                        factory = { context ->
                            YouTubePlayerView(context).apply {
                                lifecycleOwner.lifecycle.addObserver(this)
                                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                    override fun onReady(youTubePlayer: YouTubePlayer) {
                                        youTubePlayer.loadVideo(trailer.key, 0f)
                                    }
                                })
                            }
                        },
                        onRelease = { view ->
                            lifecycleOwner.lifecycle.removeObserver(view)
                            view.release()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = "No trailer available",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            is UiState.StateFailed -> {
                Text(
                    text = "Error loading video",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {}
        }

        IconButton(
            onClick = { navigator.goBack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }
    }
}
