package com.example.home.api.user.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.user.delete.request.UserDeleteRequest
import com.example.home.api.user.delete.response.UserDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.user.UserControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserDeleteApi(
    private val userControlService: UserControlService

) {
    companion object {
        const val API_PATH = "api/user/delete"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: UserDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUserId = UserId(request.userId)

        val serviceExecResult = userControlService.delete(requestUserId)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "ユーザー削除失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不正エラー.code) {
                parameter = "user_id"
                errorMessage = ResponseCode.データ不正エラー.message
            }

            if (serviceExecResult.result == ResponseCode.ユーザーエラー_グループリーダー削除.code) {
                parameter = "user_id"
                errorMessage = ResponseCode.ユーザーエラー_グループリーダー削除.message
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
        val dataObject = serviceExecResult.deleteRows ?: run { 0 }

        val userDeleteResponse =
            UserDeleteResponse(
                status,
                message,
                UserDeleteResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userDeleteResponse)
    }
}