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

    private LoginRepository loginRepository = Mock()

    private LoginService sut = new LoginService(loginRepository)

    def "Login_#useCase"() {

        setup:

        when:
        def result = sut.login(FixtureLoginInfo.正常値())

        then:
        1 * loginRepository.refer(_, _) >> userInfo
        result == expected

        where:
        useCase                | expected                                                                    | userInfo
        "正常"                 | new UserLoginResult(ResponseCode.成功.code, true)                           | FixtureUserInfo.ユーザー情報_正常()
        "異常_ユーザー_照合"   | new UserLoginResult(ResponseCode.ログインエラー_データ照合.code, false)     | null
        "異常_ユーザー_未承認" | new UserLoginResult(ResponseCode.ログインエラー_未承認ユーザー.code, false) | FixtureUserInfo.ユーザー情報_未承認()
        "異常_ユーザー_削除済" | new UserLoginResult(ResponseCode.ログインエラー_削除済ユーザー.code, false) | FixtureUserInfo.ユーザー情報_削除済()
    }
}
