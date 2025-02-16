package com.example.home.domain.entity.communication.result

import com.example.home.domain.entity.communication.Communication

data class CommunicationReferResult(
    val result: String,
    val category: List<Communication>? = null
)
