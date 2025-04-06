package com.example.home.api.communication.update


import com.example.home.api.ErrorResponse
import com.example.home.api.communication.update.request.CommunicationUpdateRequest
import com.example.home.api.communication.update.response.CommunicationUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.communication.*
import com.example.home.domain.value_object.etc.Amount
import com.example.home.service.communication.CommunicationService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class CommunicationUpdateApi(
    private val communicationService: CommunicationService

) {
    companion object {
        const val API_PATH = "api/communication/update"
    }

    @PostMapping(value = [API_PATH])
    fun update(
        @RequestBody @Valid request: CommunicationUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGiftId = GiftId(request.giftId)

        val requestDate =
            if (request.date == "") null
            else request.date?.let {
                val dates = it.split("-")
                LocalDate.of(dates[0].toInt(), dates[1].toInt(), dates[2].toInt())
            }

        val requestFrom = if (request.from == "") null else request.from?.let { GiftFrom(it) }
        val requestTo = if (request.to == "") null else request.to?.let { GiftTo(it) }
        val requestItem = if (request.item == "") null else request.item?.let { GiftItem(it) }
        val requestAmount = if (request.amount == 0) null else request.amount?.let { Amount(it) }
        val requestRemarks = if (request.remarks == "") null else request.remarks?.let { GiftRemarks(it) }

        val requestRtnDates =
            if (request.rtnDate == "") null
            else request.rtnDate?.let {
                val rtnDates = it.split("-")
                LocalDate.of(rtnDates[0].toInt(), rtnDates[1].toInt(), rtnDates[2].toInt())
            }

        val requestRtnItem = if (request.rtnItem == "") null else request.rtnItem?.let { GiftItem(it) }
        val requestRtnAmount = if (request.rtnAmount == 0) null else request.rtnAmount?.let { Amount(it) }
        val requestRtnRemarks = if (request.rtnRemarks == "") null else request.rtnRemarks?.let { GiftRemarks(it) }
        val requestRtnFlg = if (request.returnFlg == 0) null else request.returnFlg?.let { GiftReturnFlg(it) }

        val serviceExecResult = communicationService.update(
            requestGiftId,
            requestDate,
            requestFrom,
            requestTo,
            requestItem,
            requestAmount,
            requestRemarks,
            requestRtnDates,
            requestRtnItem,
            requestRtnAmount,
            requestRtnRemarks,
            requestRtnFlg
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "お付き合い帳更新失敗"
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
        val dataObject = serviceExecResult.updateRows

        val communicationUpdateResponse =
            CommunicationUpdateResponse(
                status,
                message,
                CommunicationUpdateResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(communicationUpdateResponse)
    }
}