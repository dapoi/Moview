package com.project.compose.core.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.compose.core.data.model.local.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM movies WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)
}
