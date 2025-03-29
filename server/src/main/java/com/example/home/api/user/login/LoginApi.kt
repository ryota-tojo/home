package com.example.home.api.user.login

import com.example.home.api.ErrorResponse
import com.example.home.api.user.login.request.LoginRequest
import com.example.home.api.user.login.response.LoginResponse
import com.example.home.domain.entity.user.LoginInfo
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.UserName
import com.example.home.domain.value_object.user.UserPassword
import com.example.home.service.user.LoginService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginApi(
    private val loginService: LoginService

) {
    companion object {
        const val API_PATH = "api/login"
    }

    @PostMapping(value = [API_PATH])
    fun login(
        @RequestBody @Valid request: LoginRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUserName = UserName(request.userName)
        val requestPassword = UserPassword(request.password)
        val requestLoginInfo = LoginInfo(requestUserName, requestPassword)

        val serviceExecResult = loginService.login(requestLoginInfo)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "ログイン失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.ログインエラー_データ照合.code) {
                parameter = "user_name, password"
                errorMessage = ResponseCode.ログインエラー_データ照合.message
            }
            if (serviceExecResult.result == ResponseCode.ログインエラー_削除済ユーザー.code) {
                parameter = "user_name, password"
                errorMessage = ResponseCode.ログインエラー_削除済ユーザー.message
            }
            if (serviceExecResult.result == ResponseCode.ログインエラー_未承認ユーザー.code) {
                parameter = "user_name, password"
                errorMessage = ResponseCode.ログインエラー_未承認ユーザー.message
            }

            val errorResponse =
                ErrorResponse(
                    status,
                    message,
                    ErrorResponse.DataObject(
                        parameter,
                        errorMessage,
                    )
                )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse)
        }

        // 成功時のレスポンス
        val status = "success"
        val message = ResponseCode.成功.message
        val dataObject = LoginResponse.DataObject(serviceExecResult.loginFlg!!)

        val loginResponse =
            LoginResponse(
                status,
                message,
                dataObject
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(loginResponse)
    }
}