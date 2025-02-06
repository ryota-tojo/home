package com.example.home.service.user

import com.example.home.data.group.FixtureGroupInfo
import com.example.home.data.user.FixtureLoginInfo
import com.example.home.data.user.FixtureUserInfo
import com.example.home.domain.entity.user.result.UserLoginResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.user.LoginRepository
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class LoginServiceTest extends Specification {

    private GroupInfoRepository groupInfoRepository = Mock()
    private LoginRepository loginRepository = Mock()

    private LoginService sut = new LoginService(groupInfoRepository, loginRepository)

    def "Login_#useCase"() {

        setup:

        when:
        def result = sut.login(FixtureLoginInfo.正常値())

        then:
        1 * loginRepository.refer(_, _) >> userInfo
        groupReferCnt * groupInfoRepository.refer(_, _) >> groupInfo
        result == expected

        where:
        useCase                        | expected                                                                      | userInfo                              | groupInfo                                  | groupReferCnt
        "正常" | new UserLoginResult(ResponseCode.成功.code, true) | FixtureUserInfo.ユーザー情報_正常() | [FixtureGroupInfo.所属グループ情報_正常()]                                                       | 1
        "異常_ユーザー_照合"           | new UserLoginResult(ResponseCode.ログインエラー_データ照合.code, false)       | null                                  | [FixtureGroupInfo.所属グループ情報_正常()] | 0
        "異常_ユーザー_未承認"         | new UserLoginResult(ResponseCode.ログインエラー_未承認ユーザー.code, false)   | FixtureUserInfo.ユーザー情報_未承認() | [FixtureGroupInfo.所属グループ情報_正常()] | 0
        "異常_ユーザー_削除済"         | new UserLoginResult(ResponseCode.ログインエラー_削除済ユーザー.code, false)   | FixtureUserInfo.ユーザー情報_削除済() | [FixtureGroupInfo.所属グループ情報_正常()] | 0
        "異常_所属グループ_データなし" | new UserLoginResult(ResponseCode.ログインエラー_所属グループ不正.code, false) | FixtureUserInfo.ユーザー情報_正常()   | []                                         | 1
    }
}
