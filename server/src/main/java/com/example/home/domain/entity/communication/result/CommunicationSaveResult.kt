package com.example.home.domain.entity.communication.result

import com.example.home.domain.entity.communication.Communication

data class CommunicationSaveResult(
    val result: String,
    val category: Communication? = null
)
