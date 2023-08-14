package com.example.planradar.infrastructure.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asset")
data class AssetEntity(
    @PrimaryKey(autoGenerate = false)
    val asset_id: Long,
    val title: String? = null,
    val description: String? = null,
    val imageUrl: String? = null
)
