package com.example.home.domain.repository.notice

import com.example.home.domain.entity.notice.Notice
import com.example.home.domain.value_object.notice.NoticeContent
import com.example.home.domain.value_object.notice.NoticeId
import com.example.home.domain.value_object.notice.NoticeTitle

interface NoticeRepository {
    fun refer(noticeId: NoticeId): List<Notice>
    fun referAll(): List<Notice>
    fun save(noticeTitle: NoticeTitle, noticeContent: NoticeContent): Notice
    fun update(noticeId: NoticeId, noticeTitle: NoticeTitle, noticeContent: NoticeContent): Int
    fun delete(noticeId: NoticeId): Int
}