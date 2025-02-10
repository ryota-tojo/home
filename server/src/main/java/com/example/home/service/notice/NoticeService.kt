package com.example.home.service.notice

import com.example.home.datasource.notice.NoticeRepositoryImpl
import com.example.home.domain.entity.notice.result.NoticeDeleteResult
import com.example.home.domain.entity.notice.result.NoticeReferResult
import com.example.home.domain.entity.notice.result.NoticeSaveResult
import com.example.home.domain.entity.notice.result.NoticeUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.notice.NoticeRepository
import com.example.home.domain.value_object.notice.NoticeContent
import com.example.home.domain.value_object.notice.NoticeId
import com.example.home.domain.value_object.notice.NoticeTitle
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository = NoticeRepositoryImpl()
) {
    fun refer(noticeId: NoticeId? = null): NoticeReferResult {
        val noticeList = noticeRepository.refer(noticeId)
        return NoticeReferResult(
            ResponseCode.成功.code,
            noticeList
        )
    }

    fun save(noticeTitle: NoticeTitle, noticeContent: NoticeContent): NoticeSaveResult {
        val notice = noticeRepository.save(noticeTitle, noticeContent)
        return NoticeSaveResult(
            ResponseCode.成功.code,
            notice
        )
    }

    fun update(noticeId: NoticeId, noticeTitle: NoticeTitle, noticeContent: NoticeContent): NoticeUpdateResult {
        val updateRows = noticeRepository.update(noticeId, noticeTitle, noticeContent)
        if (updateRows == 0) {
            return NoticeUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return NoticeUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(noticeId: NoticeId): NoticeDeleteResult {
        val deleteRows = noticeRepository.delete(noticeId)
        if (deleteRows == 0) {
            return NoticeDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return NoticeDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }
}