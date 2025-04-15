package com.example.home.api.master.choices.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.master.choices.refer.response.MasterChoicesReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.service.master.choices.MasterChoicesService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MasterChoicesReferApi(
    private val masterChoicesService: MasterChoicesService

) {
    companion object {
        const val API_PATH = "api/master/choices/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        val serviceExecResult = masterChoicesService.refer()

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "選択肢マスター参照失敗"
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
        val dataObject = serviceExecResult.masterChoices?.map { choices ->
            MasterChoicesReferResponse.ChoicesObject(
                choices.id,
                choices.choicesItemType.value,
                choices.choicesItemNo.value,
                choices.choicesItemNamePC.value,
                choices.choicesItemNameSP.value,
            )
        }

        val masterChoicesReferResponse =
            MasterChoicesReferResponse(
                status,
                message,
                MasterChoicesReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(masterChoicesReferResponse)
    }
}