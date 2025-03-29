package com.example.home.api.user.login

import com.example.home.api.user.login.request.LoginRequest
import com.example.home.data.user.FixtureUserInfo
import com.example.home.domain.repository.user.LoginRepository
import com.example.home.service.user.LoginService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification
import spock.lang.Unroll

class LoginApiTest extends Specification {

    private LoginRepository loginRepository = Mock(LoginRepository)
    private LoginService loginService = new LoginService(loginRepository)
    private LoginApi loginApi = new LoginApi(loginService)

    private ObjectMapper objectMapper = new ObjectMapper()

    @Unroll
    def "loginApi2_リクエスト確認_#useCase"() {

        given:
        // リクエストを生成
        def userName = "testUserName"
        def password = "testPassword"
        def loginRequest = new LoginRequest(userName, password)

        //
        loginRepository.refer(_, _) >> userInfo

        when:
        def response = loginApi.login(loginRequest, Mock(HttpServletResponse))
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
        def responseBody = objectMapper.writeValueAsString(response.body)

        def normalizedResponse = responseBody.replaceAll("\\s+", "")
        def normalizedExpectedJson = expectedJson.replaceAll("\\s+", "")

        then:
        normalizedResponse == normalizedExpectedJson

        where:
        useCase         | userInfo                              | expectedJson
        "正常系"        | FixtureUserInfo.ユーザー情報_正常()   | new File("src/test/resources/api/json/user/login_success.json").text
        "異常系_認証"   | null                                  | new File("src/test/resources/api/json/user/login_error_authentication.json").text
        "異常系_未承認" | FixtureUserInfo.ユーザー情報_未承認() | new File("src/test/resources/api/json/user/login_error_approval.json").text
        "異常系_削除済" | FixtureUserInfo.ユーザー情報_削除済() | new File("src/test/resources/api/json/user/login_error_deleted.json").text

    }
}
