package com.example.home.domain.entity.notice.result

import com.example.home.domain.entity.notice.Notice

data class NoticeReferResult(
    val result: String,
    val noticeList: List<Notice>? = null
)
