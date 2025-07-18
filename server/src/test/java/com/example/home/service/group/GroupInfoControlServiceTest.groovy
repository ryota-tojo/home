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
import com.example.home.domain.repository.user.UserInfoRepository
import spock.lang.Specification

class GroupInfoControlServiceTest extends Specification {

    private GroupInfoRepository groupInfoRepository = Mock()
    private UserInfoRepository userInfoRepository = Mock()
    private GroupInfoControlService sut = new GroupInfoControlService(groupInfoRepository, userInfoRepository)

    def "groupInfo_refer_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def userId = FixtureUserInfo.ユーザーID_正常()
        def leaderFlg = FixtureGroupInfo.リーダーフラグ_リーダー()

        when:
        def result = sut.refer(groupsId, userId, leaderFlg, null, null)

        then:
        1 * groupInfoRepository.refer(groupsId, userId, leaderFlg, null, null) >> groupInfoList
        result == expected

        where:
        useCase           | expected                                                                                         | groupInfoList
        "正常"            | new GroupInfoReferResult(ResponseCode.成功.code, [FixtureGroupInfo.所属グループ情報_メンバー()]) | [FixtureGroupInfo.所属グループ情報_メンバー()]
        "正常_データなし" | new GroupInfoReferResult(ResponseCode.データ不在エラー.code, null)                               | null
    }

    def "groupInfo_save_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def userId = FixtureUserInfo.ユーザーID_正常()

        when:
        def result = sut.save(groupsId, userId, leaderFlg)

        then:
        uiCnt * userInfoRepository.refer(userId, null, null, null, null, null, null, null, null, null, null) >> userInfo
        grCnt * groupInfoRepository.refer(groupsId, null, _, null, null) >> groupInfoRefer
        gsCnt * groupInfoRepository.save(groupsId, userId, leaderFlg) >> groupInfo
        result == expected

        where:
        useCase                               | expected                                                                                      | leaderFlg                                  | userInfo                              | groupInfoRefer                                 | groupInfo                                    | uiCnt | grCnt | gsCnt
        "正常_リーダー"                       | new GroupInfoSaveResult(ResponseCode.成功.code, FixtureGroupInfo.所属グループ情報_リーダー()) | FixtureGroupInfo.リーダーフラグ_リーダー() | [FixtureUserInfo.ユーザー情報_正常()] | []                                             | FixtureGroupInfo.所属グループ情報_リーダー() | 1     | _     | 1
        "正常_メンバー"                       | new GroupInfoSaveResult(ResponseCode.成功.code, FixtureGroupInfo.所属グループ情報_メンバー()) | FixtureGroupInfo.リーダーフラグ_メンバー() | [FixtureUserInfo.ユーザー情報_正常()] | [FixtureGroupInfo.所属グループ情報_リーダー()] | FixtureGroupInfo.所属グループ情報_メンバー() | 1     | 0     | 1
        "異常_存在しないユーザー"             | new GroupInfoSaveResult(ResponseCode.存在しないユーザー.code, null)                           | FixtureGroupInfo.リーダーフラグ_リーダー() | null                                  | [FixtureGroupInfo.所属グループ情報_リーダー()] | FixtureGroupInfo.所属グループ情報_リーダー() | 1     | 0     | 0
        "異常_既にリーダーが存在するグループ" | new GroupInfoSaveResult(ResponseCode.既にリーダーが存在するグループ.code, null)               | FixtureGroupInfo.リーダーフラグ_リーダー() | [FixtureUserInfo.ユーザー情報_正常()] | [FixtureGroupInfo.所属グループ情報_リーダー()] | FixtureGroupInfo.所属グループ情報_リーダー() | 1     | _     | 0
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
        "異常_データなし" | new GroupInfoUpdateResult(ResponseCode.データ不在エラー.code, 0) | 0
    }

    def "groupInfo_delete_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def userId = FixtureUserInfo.ユーザーID_正常()

        when:
        def result = sut.delete(groupsId, userId)

        then:
        gdCnt * groupInfoRepository.delete(groupsId, userId) >> deleteRows
        result == expected

        where:
        useCase                               | expected                                                                       | groupInfoRefer                                 | deleteRows | gdCnt
        "正常"                                | new GroupInfoDeleteResult(ResponseCode.成功.code, 1)                           | [FixtureGroupInfo.所属グループ情報_メンバー()] | 1          | 1
    }
}
