package com.project.compose.core.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.project.compose.core.data.BuildConfig.DEBUG
import com.project.compose.core.data.repository.AppRepository
import com.project.compose.core.data.repository.AppRepositoryImpl
import com.project.compose.core.data.source.local.AppDataStore
import com.project.compose.core.data.source.remote.ApiService
import androidx.room.Room
import com.project.compose.core.data.source.local.MovieDao
import com.project.compose.core.data.source.local.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(
        apiService: ApiService,
        movieDao: MovieDao
    ): AppRepository = AppRepositoryImpl(apiService, movieDao)

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db").build()

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao = database.movieDao()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(moshiConverterFactory)
        .client(okHttpClient)
        .build()

    @Provides
    fun provideOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(if (DEBUG) BODY else NONE))
        .addInterceptor(chuckerInterceptor)
        .build()

    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context
    ) = ChuckerInterceptor.Builder(context).collector(ChuckerCollector(context)).build()

    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = AppDataStore(context)
}