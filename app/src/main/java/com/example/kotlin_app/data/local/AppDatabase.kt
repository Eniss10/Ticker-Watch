package com.example.kotlin_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StockEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun stockDao(): StockDao
}