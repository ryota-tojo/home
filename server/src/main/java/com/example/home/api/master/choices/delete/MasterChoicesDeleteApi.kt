package com.example.home.api.master.choices.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.master.choices.delete.request.MasterChoicesDeleteRequest
import com.example.home.api.master.choices.delete.response.MasterChoicesDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.service.master.choices.MasterChoicesService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MasterChoicesDeleteApi(
    private val masterChoicesService: MasterChoicesService

) {
    companion object {
        const val API_PATH = "api/master/choices/delete"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: MasterChoicesDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestId = request.id

        val serviceExecResult = masterChoicesService.delete(
            requestId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "選択肢マスター削除失敗"
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
        val dataObject = serviceExecResult.deleteRows

        val masterChoicesDeleteResponse =
            MasterChoicesDeleteResponse(
                status,
                message,
                MasterChoicesDeleteResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(masterChoicesDeleteResponse)
    }
}