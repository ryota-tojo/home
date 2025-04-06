package com.example.home.api.communication.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.communication.delete.request.CommunicationDeleteRequest
import com.example.home.api.communication.delete.response.CommunicationDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.communication.GiftId
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.communication.CommunicationService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CommunicationDeleteApi(
    private val communicationService: CommunicationService

) {
    companion object {
        const val API_PATH = "api/communication/delete"
    }

    @PostMapping(value = [API_PATH])
    fun delete(
        @RequestBody @Valid request: CommunicationDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestGiftId = if (request.giftId == 0) null else request.giftId?.let { GiftId(it) }

        val serviceExecResult = communicationService.delete(
            requestGroupsId, requestGiftId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "お付き合い帳削除失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "-"
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
        val dataObject = serviceExecResult.deleteRows

        val communicationDeleteResponse =
            CommunicationDeleteResponse(
                status,
                message,
                CommunicationDeleteResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(communicationDeleteResponse)
    }
}