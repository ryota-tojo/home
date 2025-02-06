package com.example.home.data.notice


import com.example.home.domain.entity.notice.Notice
import com.example.home.domain.value_object.notice.NoticeContent
import com.example.home.domain.value_object.notice.NoticeId
import com.example.home.domain.value_object.notice.NoticeTitle

import java.time.LocalDateTime

class FixtureNotice {
    static お知らせID_正常() {
        new NoticeId(1)
    }

    static お知らせタイトル_正常() {
        new NoticeTitle("")
    }

    static お知らせコンテンツ_正常() {
        new NoticeContent("")
    }

    static お知らせ作成日_正常() {
        LocalDateTime.of(2025, 2, 2, 12, 34, 56, 789000000)
    }

    static お知らせ更新日_正常() {
        LocalDateTime.of(2025, 2, 2, 12, 34, 56, 789000000)
    }

    static お知らせ_正常値() {
        return new Notice(
                お知らせID_正常(),
                お知らせタイトル_正常(),
                お知らせコンテンツ_正常(),
                お知らせ作成日_正常(),
                お知らせ更新日_正常()
        )
    }
}