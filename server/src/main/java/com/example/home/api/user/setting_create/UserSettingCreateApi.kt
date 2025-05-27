package com.example.home.api.user.setting_create


import com.example.home.api.ErrorResponse
import com.example.home.api.group.create.request.UserSettingCreateRequest
import com.example.home.api.group.create.response.UserSettingCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserSettingKey
import com.example.home.domain.value_object.user.UserSettingValue
import com.example.home.service.user.UserControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserSettingCreateApi(
    private val userControlService: UserControlService

) {
    companion object {
        const val API_PATH = "api/user/create/setting"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: UserSettingCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUsersId = UserId(request.userId)
        val requestSettingKey = UserSettingKey(request.settingKey)
        val requestSettingValue = UserSettingValue(request.settingValue)

        val serviceExecResult =
            userControlService.userSettingSave(requestUsersId, requestSettingKey, requestSettingValue)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "ユーザー設定登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                errorMessage = ResponseCode.バリデーションエラー.message
            }
            if (serviceExecResult.result == ResponseCode.重複エラー.code) {
                errorMessage = ResponseCode.重複エラー.message
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
        val dataObject = UserSettingCreateResponse.UserSettingObject(
            serviceExecResult.userSetting?.userSettingKey?.value,
            serviceExecResult.userSetting?.userSettingvalue?.value
        )

        val groupCreateResponse =
            UserSettingCreateResponse(
                status,
                message,
                UserSettingCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupCreateResponse)
    }
}