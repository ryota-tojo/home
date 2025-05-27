package com.example.home.api.user.setting_delete


import com.example.home.api.ErrorResponse
import com.example.home.api.group.create.request.UserSettingDeleteRequest
import com.example.home.api.group.setting_delete.response.UserSettingDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserSettingKey
import com.example.home.service.user.UserControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserSettingDeleteApi(
    private val userControlService: UserControlService

) {
    companion object {
        const val API_PATH = "api/user/delete/setting"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: UserSettingDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUsersId = UserId(request.userId)
        val requestSettingKey: UserSettingKey? = request.settingKey?.let { UserSettingKey(it) }


        val serviceExecResult = userControlService.userSettingDelete(requestUsersId, requestSettingKey)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ設定削除失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "users_id"
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
        val dataObject = UserSettingDeleteResponse.DataObject(
            serviceExecResult.deleteRows
        )

        val userSettingDeleteResponse =
            UserSettingDeleteResponse(
                status,
                message,
                dataObject
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userSettingDeleteResponse)
    }
}