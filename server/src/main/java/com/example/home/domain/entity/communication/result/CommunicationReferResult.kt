package com.example.home.domain.entity.communication.result

import com.example.home.domain.entity.communication.Communication

data class CommunicationReferResult(
    val result: String,
    val communication: List<Communication>? = null
)
