package com.example.planradar.domain.model

data class History(
    val id: Long,
    val time: String? = null,
    val temp:String? = null,
    val description: String? = null
)
