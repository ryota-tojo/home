package com.example.home.api.user.update.setting


import com.example.home.api.ErrorResponse
import com.example.home.api.user.update.setting.request.UserSettingUpdateRequest
import com.example.home.api.user.update.setting.response.UserSettingUpdateResponse
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
class UserSettingUpdateApi(
    private val userControlService: UserControlService

) {
    companion object {
        const val API_PATH = "api/user/update/setting"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: UserSettingUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUserId = UserId(request.userId)
        val requestSettingKey = UserSettingKey(request.settingKey)
        val requestSettingValue = UserSettingValue(request.settingValue)

        val serviceExecResult = userControlService.userSettingUpdate(
            requestUserId, requestSettingKey, requestSettingValue
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "ユーザー設定更新失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "user_name, password"
                errorMessage = ResponseCode.バリデーションエラー.message
            }

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "user_id"
                errorMessage = ResponseCode.データ不在エラー.message
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
        val dataObject = serviceExecResult.updateRows ?: run { 0 }

        val userSettingUpdateResponse =
            UserSettingUpdateResponse(
                status,
                message,
                UserSettingUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userSettingUpdateResponse)
    }
}