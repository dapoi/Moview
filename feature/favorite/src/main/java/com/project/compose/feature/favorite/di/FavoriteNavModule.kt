package com.project.compose.feature.favorite.di

import com.project.compose.core.navigation.base.BaseNavGraph
import com.project.compose.feature.favorite.navigation.FavoriteNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoriteNavModule {
    @Binds
    @IntoSet
    abstract fun bindFavoriteNavGraph(favoriteNavGraphImpl: FavoriteNavGraphImpl): BaseNavGraph
}
