package com.example.planradar.infrastructure.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val time: String? = null,
    val temp: String? = null,
    val description: String? = null
)
