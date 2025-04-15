package com.example.home.api.master.choices.create


import com.example.home.api.ErrorResponse
import com.example.home.api.master.choices.create.request.MasterChoicesCreateRequest
import com.example.home.api.master.choices.create.response.MasterChoicesCreateResponse
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
class MasterChoicesCreateApi(
    private val masterChoicesService: MasterChoicesService

) {
    companion object {
        const val API_PATH = "api/master/choices/create"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: MasterChoicesCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestItemType = ChoicesItemType(request.itemType)
        val requestItemNo = ChoicesItemNo(request.itemNo)
        val requestItemNamePC = ChoicesItemNamePC(request.itemNamePC)
        val requestItemNameSP = ChoicesItemNameSP(request.itemNameSP)

        val serviceExecResult = masterChoicesService.save(
            requestItemType,
            requestItemNo,
            requestItemNamePC,
            requestItemNameSP,
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "選択肢マスター登録失敗"
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
        val dataObject = MasterChoicesCreateResponse.ChoicesObject(
            serviceExecResult.masterChoices?.id,
            serviceExecResult.masterChoices?.choicesItemType?.value,
            serviceExecResult.masterChoices?.choicesItemNo?.value,
            serviceExecResult.masterChoices?.choicesItemNamePC?.value,
            serviceExecResult.masterChoices?.choicesItemNameSP?.value,
        )

        val masterChoicesCreateResponse =
            MasterChoicesCreateResponse(
                status,
                message,
                MasterChoicesCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(masterChoicesCreateResponse)
    }
}