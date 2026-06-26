package com.project.compose.feature.movie.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.project.compose.core.navigation.base.BaseNavGraph
import com.project.compose.core.navigation.helper.Navigator
import com.project.compose.core.navigation.route.HomeGraph.MovieDetailRoute
import com.project.compose.core.navigation.route.HomeGraph.MovieLandingRoute
import com.project.compose.core.navigation.route.HomeGraph.MoviePlayerRoute
import com.project.compose.feature.movie.screen.MovieDetailScreen
import com.project.compose.feature.movie.screen.MovieLandingScreen
import com.project.compose.feature.movie.screen.MoviePlayerScreen
import javax.inject.Inject

class MovieNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<MovieLandingRoute> { MovieLandingScreen(navigator) }
        entry<MovieDetailRoute> { args -> MovieDetailScreen(args, navigator) }
        entry<MoviePlayerRoute> { args -> MoviePlayerScreen(args, navigator) }
    }
}