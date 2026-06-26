package com.project.compose.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface HomeGraph : NavKey {
    @Serializable
    data object MovieLandingRoute : HomeGraph

    @Serializable
    data class MovieDetailRoute(val movieId: Int) : HomeGraph

    @Serializable
    data class MoviePlayerRoute(val movieId: Int) : HomeGraph
}