package com.example.planradar.infrastructure.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val icon: String,
    val description: String,
    val temp: Double,
    val windSpeed: Double,
    val humidity: Int,
    val timezone: String,
    val name: String,
)