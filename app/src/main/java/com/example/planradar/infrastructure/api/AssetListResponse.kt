package com.example.planradar.infrastructure.api

import com.google.gson.annotations.SerializedName

data class AssetListResponse(
    @SerializedName("items")
    val items: List<AssetResponse>,
)

data class AssetResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = null,
)