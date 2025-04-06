package com.example.home.api.communication.create


import com.example.home.api.ErrorResponse
import com.example.home.api.communication.create.request.CommunicationCreateRequest
import com.example.home.api.communication.create.response.CommunicationCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.communication.*
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.communication.CommunicationService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class CommunicationCreateApi(
    private val communicationService: CommunicationService
) {
    companion object {
        const val API_PATH = "api/communication/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: CommunicationCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)

        val requestDates = request.date.split("-")
        val requestDate = LocalDate.of(requestDates[0].toInt(), requestDates[1].toInt(), requestDates[2].toInt())

        val requestFrom = GiftFrom(request.from)
        val requestTo = GiftTo(request.to)
        val requestItem = GiftItem(request.item)
        val requestAmount = Amount(request.amount)
        val requestRemarks = GiftRemarks(request.remarks)

        val requestRtnDates = request.rtnDate.split("-")
        val requestRtnDate =
            LocalDate.of(requestRtnDates[0].toInt(), requestRtnDates[1].toInt(), requestRtnDates[2].toInt())

        val requestRtnItem = GiftItem(request.rtnItem)
        val requestRtnAmount = Amount(request.rtnAmount)
        val requestRtnRemarks = GiftRemarks(request.rtnRemarks)

        val requestRtnFlg = GiftReturnFlg(request.returnFlg)

        val serviceExecResult = communicationService.save(
            requestGroupsId,
            requestDate,
            requestFrom,
            requestTo,
            requestItem,
            requestAmount,
            requestRemarks,
            requestRtnDate,
            requestRtnItem,
            requestRtnAmount,
            requestRtnRemarks,
            requestRtnFlg
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "お付き合い帳登録失敗"
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
        val dataObject = CommunicationCreateResponse.CommunicationObject(
            serviceExecResult.communication?.id?.value,
            serviceExecResult.communication?.groupsId?.value,
            parseLocalDateTime(serviceExecResult.communication?.giftDate?.toString(), "yyyy-MM-dd"),
            serviceExecResult.communication?.giftFrom?.value,
            serviceExecResult.communication?.giftTo?.value,
            serviceExecResult.communication?.giftItem?.value,
            serviceExecResult.communication?.giftAmount?.value,
            serviceExecResult.communication?.giftRemarks?.value,
            parseLocalDateTime(serviceExecResult.communication?.giftRtnDate?.toString(), "yyyy-MM-dd"),
            serviceExecResult.communication?.giftRtnItem?.value,
            serviceExecResult.communication?.giftRtnAmount?.value,
            serviceExecResult.communication?.giftRtnRemarks?.value,
            serviceExecResult.communication?.returnFlg?.value,
        )

        val communicationCreateResponse =
            CommunicationCreateResponse(
                status,
                message,
                CommunicationCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(communicationCreateResponse)
    }
}