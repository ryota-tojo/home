package com.example.home.api.communication.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.communication.refer.request.CommunicationReferRequest
import com.example.home.api.communication.refer.response.CommunicationReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.communication.GiftId
import com.example.home.service.communication.CommunicationService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CommunicationReferApi(
    private val communicationService: CommunicationService

) {
    companion object {
        const val API_PATH = "api/communication/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: CommunicationReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGiftId = if (request.giftId == null) null else GiftId(request.giftId)

        val serviceExecResult = communicationService.refer(
            requestGiftId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "お付き合い帳参照失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

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
        val dataObject = serviceExecResult.communication?.map { communication ->
            CommunicationReferResponse.CommunicationObject(
                communication.id.value,
                communication.groupsId.value,
                parseLocalDateTime(communication.giftDate.toString(), "yyyy-MM-dd"),
                communication.giftFrom.value,
                communication.giftTo.value,
                communication.giftItem.value,
                communication.giftAmount.value,
                communication.giftRemarks.value,
                parseLocalDateTime(communication.giftRtnDate.toString(), "yyyy-MM-dd"),
                communication.giftRtnItem.value,
                communication.giftRtnAmount.value,
                communication.giftRtnRemarks.value,
                communication.returnFlg.value,
            )
        }

        val communicationReferResponse =
            CommunicationReferResponse(
                status,
                message,
                CommunicationReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(communicationReferResponse)
    }
}