package com.example.planradar.infrastructure.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(assets: HistoryEntity)

    @Query("SELECT * FROM history WHERE name = :nameOfCity")
    suspend fun getHistory(nameOfCity: String): List<HistoryEntity>
}