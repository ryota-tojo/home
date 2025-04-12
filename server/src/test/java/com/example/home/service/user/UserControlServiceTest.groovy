package com.example.home.service.user

import com.example.home.data.group.FixtureGroupInfo
import com.example.home.data.group.FixtureGroupList
import com.example.home.data.user.FixtureUserInfo
import com.example.home.data.user.FixtureUserRefer
import com.example.home.data.user.FixtureUserSetting
import com.example.home.domain.entity.user.result.UserDeleteResult
import com.example.home.domain.entity.user.result.UserReferResult
import com.example.home.domain.entity.user.result.UserSaveResult
import com.example.home.domain.entity.user.result.UserUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.user.UserInfoRepository
import com.example.home.domain.repository.user.UserSettingRepository
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class UserControlServiceTest extends Specification {
    private UserInfoRepository userInfoRepository = Mock()
    private UserSettingRepository userSettingRepository = Mock()
    private GroupInfoRepository groupInfoRepository = Mock()

    private UserControlService sut = new UserControlService(userInfoRepository, userSettingRepository, groupInfoRepository)

    def "userInfo_refer_#useCase"() {
        setup:

        when:
        def result = sut.refer(userId, userName, null, null)

        then:
        1 * userInfoRepository.refer(_, _, null, null) >> userList
        usrCnt * userSettingRepository.refer(_, null) >> FixtureUserSetting.ユーザー設定_正常()
        gigCnt * groupInfoRepository.getGroupsId(_) >> groupsId
        girCnt * groupInfoRepository.refer(_, _, null, null) >> groupInfo
        result == expected

        where:
        useCase                    | expected                                                                                            | userId                            | userName                          | userList                              | groupsId                               | groupInfo                                      | usrCnt | gigCnt | girCnt
        "正常_idあり_groupsIdあり" | new UserReferResult(ResponseCode.成功.code, [FixtureUserRefer.ユーザー参照_正常()])                 | FixtureUserInfo.ユーザーID_正常() | FixtureUserInfo.ユーザー名_正常() | [FixtureUserInfo.ユーザー情報_正常()] | FixtureGroupList.所属グループID_正常() | [FixtureGroupInfo.所属グループ情報_メンバー()] | 1      | 1      | 1
        "正常_idなし_groupsIdあり" | new UserReferResult(ResponseCode.成功.code, [FixtureUserRefer.ユーザー参照_正常()])                 | null                              | FixtureUserInfo.ユーザー名_正常() | [FixtureUserInfo.ユーザー情報_正常()] | FixtureGroupList.所属グループID_正常() | [FixtureGroupInfo.所属グループ情報_メンバー()] | 1      | 1      | 1
        "正常_idなし_groupsIdなし" | new UserReferResult(ResponseCode.成功.code, [FixtureUserRefer.ユーザー参照_正常()])                 | null                              | null                              | [FixtureUserInfo.ユーザー情報_正常()] | FixtureGroupList.所属グループID_正常() | [FixtureGroupInfo.所属グループ情報_メンバー()] | 1      | 1      | 1
        "正常_グループ情報なし"    | new UserReferResult(ResponseCode.成功.code, [FixtureUserRefer.ユーザー参照_所属グループ情報なし()]) | null                              | null                              | [FixtureUserInfo.ユーザー情報_正常()] | null                                   | null                                           | 1      | 1      | 1
        "正常_ユーザーなし" | new UserReferResult(ResponseCode.データ不在エラー.code, null) | null | null | [] | FixtureGroupList.所属グループID_正常() | [FixtureGroupInfo.所属グループ情報_メンバー()] | 0 | 0 | 0
    }

    def "userInfo_save_#useCase"() {
        setup:
        def userId = FixtureUserInfo.ユーザーID_正常()
        def userSettingKey = FixtureUserSetting.設定キー_正常()
        def userSettingValue = FixtureUserSetting.設定キー_正常()
        def groupsId = FixtureGroupList.所属グループID_正常()
        def groupPassword = FixtureGroupList.所属グループパスワード_正常()
        def password = FixtureUserInfo.パスワード_正常()
        def permission = FixtureUserInfo.権限フラグ_一般()
        def approvalFlg = FixtureUserInfo.承認フラグ_未承認()
        def deleteFlg = FixtureUserInfo.削除フラグ_未削除()

        when:
        def result = sut.save(userName, password, permission, approvalFlg, deleteFlg
        )

        then:
        dupCnt * userInfoRepository.isDuplication(_) >> dup
        uisCnt * userInfoRepository.save(userName, password, permission, approvalFlg, deleteFlg
        ) >> FixtureUserInfo.ユーザー情報_正常()
        usrCnt * userSettingRepository.save(_, _, _) >> []
        result == expected

        where:
        useCase                     | expected                                                                                                          | userName                                          | dup   | cecr | dupCnt | cerCnt | uisCnt | usrCnt
        "正常"                      | new UserSaveResult(ResponseCode.成功.code, FixtureUserRefer.ユーザー参照_ユーザー設定なし_所属グループ情報なし()) | FixtureUserInfo.ユーザー名_正常()                 | false | true | 1      | 1      | 1      | _
        "異常_バリデーションエラー" | new UserSaveResult(ResponseCode.バリデーションエラー.code, null)                                                  | FixtureUserInfo.ユーザー名_バリデーションエラー() | false | true | 0      | 0      | 0      | 0
        "異常_重複エラー"           | new UserSaveResult(ResponseCode.重複エラー.code, null)                                                            | FixtureUserInfo.ユーザー名_正常()                 | true  | _    | 1      | 0      | 0      | 0

    }

    def "userInfo_userInfoUpdate_#useCase"() {
        setup:
        def userId = FixtureUserInfo.ユーザーID_正常()
        def password = FixtureUserInfo.パスワード_正常()
        def permission = FixtureUserInfo.権限フラグ_一般()
        def approvalFlg = FixtureUserInfo.承認フラグ_未承認()
        def deleteFlg = FixtureUserInfo.削除フラグ_未削除()

        when:
        def result = sut.userInfoUpdate(userId, userName, password, permission, approvalFlg, deleteFlg)

        then:
        updateCnt * userInfoRepository.update(userId, userName, password, permission, approvalFlg, deleteFlg) >> updateRows
        result == expected

        where:
        useCase                     | expected                                                        | userName                                          | updateRows | updateCnt
        "正常"                      | new UserUpdateResult(ResponseCode.成功.code, 1)                 | FixtureUserInfo.ユーザー名_正常()                 | 1          | 1
        "異常_バリデーションエラー" | new UserUpdateResult(ResponseCode.バリデーションエラー.code, 0) | FixtureUserInfo.ユーザー名_バリデーションエラー() | 0          | 0
        "異常_データ不在エラー"     | new UserUpdateResult(ResponseCode.データ不在エラー.code, 0)     | FixtureUserInfo.ユーザー名_正常()                 | 0          | 1

    }

    def "userInfo_userSettingUpdate_#useCase"() {
        setup:
        def userId = FixtureUserInfo.ユーザーID_正常()
        def settingKey = FixtureUserSetting.設定キー_正常()

        when:
        def result = sut.userSettingUpdate(userId, settingKey, settingValue)

        then:
        updateCnt * userSettingRepository.update(userId, settingKey, settingValue) >> updateRows
        result == expected

        where:
        useCase                     | expected                                                        | settingValue                                     | updateRows | updateCnt
        "正常"                      | new UserUpdateResult(ResponseCode.成功.code, 1)                 | FixtureUserSetting.設定値_正常()                 | 1          | 1
        "異常_バリデーションエラー" | new UserUpdateResult(ResponseCode.バリデーションエラー.code, 0) | FixtureUserSetting.設定値_バリデーションエラー() | 0          | 0
        "異常_データ不在エラー"     | new UserUpdateResult(ResponseCode.データ不在エラー.code, 0)     | FixtureUserSetting.設定値_正常()                 | 0          | 1

    }

    def "userInfo_delete_#useCase"() {
        setup:
        def userId = FixtureUserInfo.ユーザーID_正常()

        when:
        def result = sut.delete(userId)

        then:
        1 * groupInfoRepository.getGroupsId(userId) >> groupsId
        girCnt * groupInfoRepository.refer(groupsId, userId, null, null) >> groupInfoList
        gidCnt * groupInfoRepository.delete(null, userId) >> giDelete
        usdCnt * userSettingRepository.delete(userId) >> usDelete
        uidCnt * userInfoRepository.delete(userId) >> uiDelete
        result == expected

        where:
        useCase                     | expected                                                                       | groupsId                               | groupInfoList                                  | giDelete | usDelete | uiDelete | girCnt | gidCnt | usdCnt | uidCnt
        "正常"                  | new UserDeleteResult(ResponseCode.成功.code, 1)             | FixtureGroupList.所属グループID_正常() | [FixtureGroupInfo.所属グループ情報_メンバー()] | 1 | 1 | 1 | 1 | 1 | 1 | 1
        "異常_グループIDなし"   | new UserDeleteResult(ResponseCode.成功.code, 1)             | null                                   | null                                           | 0 | 1 | 1 | 0 | 0 | 1 | 1
        "異常_グループ情報なし"     | new UserDeleteResult(ResponseCode.データ不正エラー.code, 0)                    | FixtureGroupList.所属グループID_正常() | null                                           | 0        | 0        | 0        | 1      | 0      | 0      | 0
        "異常_グループリーダー削除" | new UserDeleteResult(ResponseCode.ユーザーエラー_グループリーダー削除.code, 0) | FixtureGroupList.所属グループID_正常() | [FixtureGroupInfo.所属グループ情報_リーダー()] | 0        | 0        | 0        | 1      | 0      | 0      | 0
        "異常_データ不在エラー" | new UserDeleteResult(ResponseCode.データ不在エラー.code, 0) | FixtureGroupList.所属グループID_正常() | [FixtureGroupInfo.所属グループ情報_メンバー()] | 0 | 0 | 0 | 1 | 1 | 1 | 1
    }
}

