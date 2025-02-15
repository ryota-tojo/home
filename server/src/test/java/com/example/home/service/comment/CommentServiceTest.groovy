package com.example.home.service.comment


import com.example.home.data.comment.FixtureComment
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.comment.result.CommentDeleteResult
import com.example.home.domain.entity.comment.result.CommentReferResult
import com.example.home.domain.entity.comment.result.CommentSaveResult
import com.example.home.domain.entity.comment.result.CommentUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.comment.CommentRepository
import spock.lang.Specification

class CommentServiceTest extends Specification {
    private CommentRepository commentRepository = Mock()
    private CommentService sut = new CommentService(commentRepository)

    def "comment_refer_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def yyyy = FixtureEtc.年_正常()
        def mm = FixtureEtc.月_正常()

        when:
        def result = sut.refer(groupsId, yyyy, mm)

        then:
        1 * commentRepository.refer(groupsId, yyyy, mm) >> commentList
        result == expected

        where:
        useCase           | expected                                                                         | commentList
        "正常"            | new CommentReferResult(ResponseCode.成功.code, [FixtureComment.コメント_正常()]) | [FixtureComment.コメント_正常()]
        "正常_データなし" | new CommentReferResult(ResponseCode.成功.code, [])                               | []
    }

    def "comment_save_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def yyyy = FixtureEtc.年_正常()
        def mm = FixtureEtc.月_正常()
        def content = FixtureComment.コンテンツ_正常()

        when:
        def result = sut.save(groupsId, yyyy, mm, content)

        then:
        1 * commentRepository.save(groupsId, yyyy, mm, content) >> comment
        result == expected

        where:
        useCase | expected                                                                      | comment
        "正常"  | new CommentSaveResult(ResponseCode.成功.code, FixtureComment.コメント_正常()) | FixtureComment.コメント_正常()
    }

    def "comment_update_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def yyyy = FixtureEtc.年_正常()
        def mm = FixtureEtc.月_正常()
        def content = FixtureComment.コンテンツ_正常()

        when:
        def result = sut.update(groupsId, yyyy, mm, content)

        then:
        1 * commentRepository.update(groupsId, yyyy, mm, content) >> updateRows
        result == expected

        where:
        useCase                 | expected                                                       | updateRows
        "正常"                  | new CommentUpdateResult(ResponseCode.成功.code, 1)             | 1
        "異常_データ不在エラー" | new CommentUpdateResult(ResponseCode.データ不在エラー.code, 0) | 0
    }

    def "comment_delete_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def yyyy = FixtureEtc.年_正常()
        def mm = FixtureEtc.月_正常()

        when:
        def result = sut.delete(groupsId, yyyy, mm)

        then:
        1 * commentRepository.delete(groupsId, yyyy, mm) >> deleteRows
        result == expected

        where:
        useCase                 | expected                                                       | deleteRows
        "正常"                  | new CommentDeleteResult(ResponseCode.成功.code, 1)             | 1
        "異常_データ不在エラー" | new CommentDeleteResult(ResponseCode.データ不在エラー.code, 0) | 0
    }
}
