package com.project.compose.feature.movie.di

import com.project.compose.core.navigation.base.BaseNavGraph
import com.project.compose.feature.movie.navigation.MovieNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieNavModule {

    @Binds
    @IntoSet
    abstract fun bindHomeNavGraph(navGraph: MovieNavGraphImpl): BaseNavGraph
}