package com.example.home.api.user.update.info


import com.example.home.api.ErrorResponse
import com.example.home.api.user.update.info.request.UserInfoUpdateRequest
import com.example.home.api.user.update.info.response.UserInfoUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.*
import com.example.home.service.user.UserControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserInfoUpdateApi(
    private val userControlService: UserControlService

) {
    companion object {
        const val API_PATH = "api/user/update/info"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: UserInfoUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUserId = UserId(request.userId)
        val requestUserName = if (request.userName == "") null else request.userName?.let { UserName(it) }
        val requestPassword = if (request.password == "") null else request.password?.let { UserPassword(it) }
        val requestPermission = if (request.permission == null) null else UserPermission(request.permission)
        val requestApproval = if (request.approval == null) null else UserApprovalFlg(request.approval)
        val requestDelete = if (request.delete == null) null else UserDeleteFlg(request.delete)

        val serviceExecResult = userControlService.userInfoUpdate(
            requestUserId, requestUserName, requestPassword, requestPermission, requestApproval, requestDelete
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "ユーザー情報更新失敗"
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

        val userInfoUpdateResponse =
            UserInfoUpdateResponse(
                status,
                message,
                UserInfoUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userInfoUpdateResponse)
    }
}