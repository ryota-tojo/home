package com.example.home.service.notice


import com.example.home.data.notice.FixtureNotice
import com.example.home.domain.entity.notice.result.NoticeDeleteResult
import com.example.home.domain.entity.notice.result.NoticeReferResult
import com.example.home.domain.entity.notice.result.NoticeSaveResult
import com.example.home.domain.entity.notice.result.NoticeUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.notice.NoticeRepository
import spock.lang.Specification

class NoticeServiceTest extends Specification {
    private NoticeRepository noticeRepository = Mock()
    private NoticeService sut = new NoticeService(noticeRepository)

    def "notice_refer_#useCase"() {

        setup:

        when:
        def result = sut.refer(FixtureNotice.お知らせID_正常())

        then:
        1 * noticeRepository.refer(FixtureNotice.お知らせID_正常()) >> noticeList
        result == expected

        where:
        useCase             | expected                                                                                         | noticeList
        "正常"              | new NoticeReferResult(ResponseCode.成功.code, [FixtureNotice.お知らせ_正常値()])                 | [FixtureNotice.お知らせ_正常値()]
        "正常_お知らせなし" | new NoticeReferResult(sprintf(ResponseCode.成功_条件付き.code, ["NOTICE_DATA_NOT_FOUND"]), null) | null
    }

    def "notice_referAll_#useCase"() {

        setup:

        when:
        def result = sut.referAll()

        then:
        1 * noticeRepository.referAll() >> noticeList
        result == expected

        where:
        useCase             | expected                                                                                         | noticeList
        "正常"              | new NoticeReferResult(ResponseCode.成功.code, [FixtureNotice.お知らせ_正常値()])                 | [FixtureNotice.お知らせ_正常値()]
        "正常_お知らせなし" | new NoticeReferResult(sprintf(ResponseCode.成功_条件付き.code, ["NOTICE_DATA_NOT_FOUND"]), null) | null
    }

    def "notice_save_#useCase"() {

        setup:
        def title = FixtureNotice.お知らせタイトル_正常()
        def content = FixtureNotice.お知らせコンテンツ_正常()

        when:
        def result = sut.save(title, content)

        then:
        1 * noticeRepository.save(title, content) >> FixtureNotice.お知らせ_正常値()
        result == expected

        where:
        useCase | expected
        "正常"  | new NoticeSaveResult(ResponseCode.成功.code, FixtureNotice.お知らせ_正常値())
    }

    def "notice_update_#useCase"() {

        setup:
        def id = FixtureNotice.お知らせID_正常()
        def title = FixtureNotice.お知らせタイトル_正常()
        def content = FixtureNotice.お知らせコンテンツ_正常()

        when:
        def result = sut.update(id, title, content)

        then:
        updateCnt * noticeRepository.update(id, title, content) >> updateRows
        result == expected

        where:
        useCase                 | expected                                                      | updateRows | updateCnt
        "正常"                  | new NoticeUpdateResult(ResponseCode.成功.code, 1)             | 1          | 1
        "異常_データ不在エラー" | new NoticeUpdateResult(ResponseCode.データ不在エラー.code, 0) | 0          | 1
    }

    def "notice_delete_#useCase"() {

        setup:
        def id = FixtureNotice.お知らせID_正常()

        when:
        def result = sut.delete(id)

        then:
        deleteCnt * noticeRepository.delete(id) >> deleteRows
        result == expected

        where:
        useCase                 | expected                                                      | deleteRows | deleteCnt
        "正常"                  | new NoticeDeleteResult(ResponseCode.成功.code, 1)             | 1          | 1
        "異常_データ不在エラー" | new NoticeDeleteResult(ResponseCode.データ不在エラー.code, 0) | 0          | 1
    }
}
