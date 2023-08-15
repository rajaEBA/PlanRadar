package com.example.planradar.infrastructure.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history")
    suspend fun getHistory(): List<HistoryEntity>
}