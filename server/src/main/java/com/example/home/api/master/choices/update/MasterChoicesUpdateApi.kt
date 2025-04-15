package com.example.home.api.master.choices.update


import com.example.home.api.ErrorResponse
import com.example.home.api.master.choices.update.request.MasterChoicesUpdateRequest
import com.example.home.api.master.choices.update.response.MasterChoicesUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.master.ChoicesItemNamePC
import com.example.home.domain.value_object.master.ChoicesItemNameSP
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType
import com.example.home.service.master.choices.MasterChoicesService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MasterChoicesUpdateApi(
    private val masterChoicesService: MasterChoicesService

) {
    companion object {
        const val API_PATH = "api/master/choices/update"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: MasterChoicesUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestId = request.id
        val requestItemType = ChoicesItemType(request.itemType)
        val requestItemNo = ChoicesItemNo(request.itemNo)
        val requestItemNamePC = ChoicesItemNamePC(request.itemNamePC)
        val requestItemNameSP = ChoicesItemNameSP(request.itemNameSP)

        val serviceExecResult = masterChoicesService.update(
            requestId,
            requestItemType,
            requestItemNo,
            requestItemNamePC,
            requestItemNameSP,
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "選択肢マスター更新失敗"
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
        val dataObject = serviceExecResult.updateRows

        val masterChoicesUpdateResponse =
            MasterChoicesUpdateResponse(
                status,
                message,
                MasterChoicesUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(masterChoicesUpdateResponse)
    }
}