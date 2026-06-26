package com.project.compose.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface FavoriteGraph : NavKey {
    @Serializable
    data object FavoriteLandingRoute : FavoriteGraph
}