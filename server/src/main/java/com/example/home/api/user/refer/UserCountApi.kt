package com.example.home.api.user.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.RecodeCountResponse
import com.example.home.api.user.refer.request.UserReferRequest
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.*
import com.example.home.service.user.UserControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserCountApi(
    private val userControlService: UserControlService

) {
    companion object {
        const val API_PATH = "api/user/count"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: UserReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUserId = if (request.userId == 0) null else request.userId?.let { UserId(it) }
        val requestUserName = if (request.userName == "") null else request.userName?.let { UserName(it) }
        val requestPermission = request.permission?.let { UserPermission(it) }
        val requestApproval = request.approval?.let { UserApprovalFlg(it) }
        val requestDeleted = request.deleted?.let { UserDeleteFlg(it) }
        val requestGroupAffiliation = request.groupAffiliation

        val requestOffset = request.offSet
        val requestLimit = request.limit

        val serviceExecResult = userControlService.refer(
            requestUserId,
            requestUserName,
            requestPermission,
            requestApproval,
            requestDeleted,
            requestGroupAffiliation,
            requestOffset,
            requestLimit
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "ユーザー参照失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "user_id, user_name"
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
        val dataObject = serviceExecResult.userRefer?.size

        val recodeCountResponse =
            RecodeCountResponse(
                status,
                message,
                RecodeCountResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(recodeCountResponse)
    }
}