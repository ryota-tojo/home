package com.example.home.domain.entity.notice.result

import com.example.home.domain.entity.notice.Notice

data class NoticeSaveResult(
    val result: String,
    val notice: Notice? = null
)
