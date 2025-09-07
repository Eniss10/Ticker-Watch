package com.example.kotlin_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
    @Query("SELECT * FROM StockEntity")
    suspend fun getAll(): List<StockEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stocks: Array<StockEntity>)

    @Delete
    suspend fun delete(stock: StockEntity)
}