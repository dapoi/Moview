package com.project.compose.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.compose.core.data.model.local.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
