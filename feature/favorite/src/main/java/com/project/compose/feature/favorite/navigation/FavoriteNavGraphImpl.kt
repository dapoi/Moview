package com.project.compose.feature.favorite.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.project.compose.core.navigation.base.BaseNavGraph
import com.project.compose.core.navigation.helper.Navigator
import com.project.compose.core.navigation.route.FavoriteGraph.FavoriteLandingRoute
import com.project.compose.feature.favorite.screen.FavoriteLandingScreen
import javax.inject.Inject

class FavoriteNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<FavoriteLandingRoute> { FavoriteLandingScreen(navigator) }
    }
}
