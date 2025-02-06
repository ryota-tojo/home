package com.example.home.service.user

import com.example.home.data.FixtureGroupInfo
import com.example.home.data.FixtureGroupList
import com.example.home.data.FixtureUserInfo
import com.example.home.data.FixtureUserSetting
import com.example.home.domain.entity.user.result.UserReferAllResult
import com.example.home.domain.entity.user.result.UserReferResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.repository.user.UserSettingRepository
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class UserControlServiceTest extends Specification {
    private UserInfoRepository userInfoRepository = Mock()
    private UserSettingRepository userSettingRepository = Mock()
    private GroupListRepository groupListRepository = Mock()
    private GroupInfoRepository groupInfoRepository = Mock()

    private UserControlService sut = new UserControlService(userInfoRepository, userSettingRepository, groupListRepository, groupInfoRepository)

    def "refer_#useCase"() {
        setup:

        when:
        def result = sut.refer(userId, userName)

        then:
        userInfoReferCnt * userInfoRepository.refer(_, _) >> userInfo
        userSettingReferAllCnt * userSettingRepository.referAll(_) >> FixtureUserSetting.ユーザー設定_正常()
        getGroupsIdCnt * groupInfoRepository.getGroupsId(_) >> groupsId
        groupInfoReferCnt * groupInfoRepository.refer(_, _) >> groupinfo

        result == expected

        where:
        useCase                         | expected                                                                                                                                                                   | userId                            | userName                                          | userInfoReferCnt | userSettingReferAllCnt | getGroupsIdCnt | groupInfoReferCnt | userInfo                              | groupsId                               | groupinfo
        "正常"                          | new UserReferResult(ResponseCode.成功.code, FixtureUserInfo.ユーザー情報_正常(), FixtureUserSetting.ユーザー設定_正常(), [FixtureGroupInfo.所属グループ情報_正常()])       | FixtureUserInfo.ユーザーID_正常() | FixtureUserInfo.ユーザー名_正常()                 | 1                | 1                      | 1              | 1                 | [FixtureUserInfo.ユーザー情報_正常()] | FixtureGroupList.所属グループID_正常() | [FixtureGroupInfo.所属グループ情報_正常()]
        "正常_ユーザーが存在しない"     | new UserReferResult(sprintf(ResponseCode.成功_条件付き.code, ["USER_NOT_FOUND"]), null, null, null)                                                                        | FixtureUserInfo.ユーザーID_正常() | FixtureUserInfo.ユーザー名_正常()                 | 1                | 0                      | 0              | 0                 | []                                    | null                                   | null
        "正常_グループ情報が存在しない" | new UserReferResult(sprintf(ResponseCode.成功_条件付き.code, ["NO_GROUP_AFFILIATION"]), FixtureUserInfo.ユーザー情報_正常(), FixtureUserSetting.ユーザー設定_正常(), null) | FixtureUserInfo.ユーザーID_正常() | FixtureUserInfo.ユーザー名_正常()                 | 1                | 1                      | 1              | 0                 | [FixtureUserInfo.ユーザー情報_正常()] | null                                   | _
        "異常_キー設定エラー"           | new UserReferResult(ResponseCode.キー未設定エラー.code, null, null, null)                                                                                                  | null                              | null                                              | 0                | 0                      | 0              | 0                 | _                                     | _                                      | _
        "異常_バリデーションエラー"     | new UserReferResult(ResponseCode.バリデーションエラー.code, null, null, null)                                                                                              | FixtureUserInfo.ユーザーID_正常() | FixtureUserInfo.ユーザー名_バリデーションエラー() | 0                | 0                      | 0              | 0                 | null                                  | null                                   | null
    }

    def "referAll_#useCase"() {
        setup:

        when:
        def result = sut.referAll()

        then:
        1 * userInfoRepository.referAll() >> userInfo

        result == expected

        where:
        useCase                     | expected                                                                                   | userInfo
        "正常"                      | new UserReferAllResult(ResponseCode.成功.code, [FixtureUserInfo.ユーザー情報_正常()])      | [FixtureUserInfo.ユーザー情報_正常()]
        "正常_ユーザーが存在しない" | new UserReferAllResult(sprintf(ResponseCode.成功_条件付き.code, ["USER_NOT_FOUND"]), null) | null
    }

}

