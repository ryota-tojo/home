package com.example.home.domain.notice

import com.example.home.domain.value_object.notice.NoticeContent
import com.example.home.domain.value_object.notice.NoticeTitle
import java.time.LocalDateTime

data class Notice(
    val noticeTitle: NoticeTitle,
    val noticeContent: NoticeContent,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime
)
