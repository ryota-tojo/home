package com.example.home.service.group

import com.example.home.data.group.FixtureGroupInfo
import com.example.home.data.group.FixtureGroupList
import com.example.home.data.user.FixtureUserInfo
import com.example.home.domain.entity.group.result.GroupInfoDeleteResult
import com.example.home.domain.entity.group.result.GroupInfoReferResult
import com.example.home.domain.entity.group.result.GroupInfoSaveResult
import com.example.home.domain.entity.group.result.GroupInfoUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import spock.lang.Specification

class GroupInfoControlServiceTest extends Specification {

    private GroupInfoRepository groupInfoRepository = Mock()
    private GroupInfoControlService sut = new GroupInfoControlService(groupInfoRepository)

    def "groupInfo_refer_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def userId = FixtureUserInfo.ユーザーID_正常()

        when:
        def result = sut.refer(groupsId, userId)

        then:
        1 * groupInfoRepository.refer(groupsId, userId) >> groupInfoList
        result == expected

        where:
        useCase           | expected                                                                                         | groupInfoList
        "正常"            | new GroupInfoReferResult(ResponseCode.成功.code, [FixtureGroupInfo.所属グループ情報_メンバー()]) | [FixtureGroupInfo.所属グループ情報_メンバー()]
        "正常_データなし" | new GroupInfoReferResult(ResponseCode.成功.code, null)                                           | null
    }

    def "groupInfo_save_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def userId = FixtureUserInfo.ユーザーID_正常()

        when:
        def result = sut.save(groupsId, userId, leaderFlg)

        then:
        1 * groupInfoRepository.save(groupsId, userId, leaderFlg) >> groupInfo
        result == expected

        where:
        useCase         | expected                                                                                      | leaderFlg                                  | groupInfo
        "正常_リーダー" | new GroupInfoSaveResult(ResponseCode.成功.code, FixtureGroupInfo.所属グループ情報_リーダー()) | FixtureGroupInfo.リーダーフラグ_リーダー() | FixtureGroupInfo.所属グループ情報_リーダー()
        "正常_メンバー" | new GroupInfoSaveResult(ResponseCode.成功.code, FixtureGroupInfo.所属グループ情報_メンバー()) | FixtureGroupInfo.リーダーフラグ_メンバー() | FixtureGroupInfo.所属グループ情報_メンバー()
    }

    def "groupInfo_update_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def userId = FixtureUserInfo.ユーザーID_正常()
        def leaderFlg = FixtureGroupInfo.リーダーフラグ_リーダー()
        def approvalFlg = FixtureGroupInfo.承認フラグ_承認()

        when:
        def result = sut.update(groupsId, userId, leaderFlg, approvalFlg)

        then:
        1 * groupInfoRepository.update(groupsId, userId, leaderFlg, approvalFlg) >> updateRows
        result == expected

        where:
        useCase           | expected                                                         | updateRows
        "正常"            | new GroupInfoUpdateResult(ResponseCode.成功.code, 1)             | 1
        "正常_データなし" | new GroupInfoUpdateResult(ResponseCode.データ不在エラー.code, 0) | 0
    }

    def "groupInfo_delete_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def userId = FixtureUserInfo.ユーザーID_正常()

        when:
        def result = sut.delete(groupsId, userId)

        then:
        1 * groupInfoRepository.delete(groupsId, userId) >> deleteRows
        result == expected

        where:
        useCase           | expected                                                         | deleteRows
        "正常"            | new GroupInfoDeleteResult(ResponseCode.成功.code, 1)             | 1
        "正常_データなし" | new GroupInfoDeleteResult(ResponseCode.データ不在エラー.code, 0) | 0
    }
}
