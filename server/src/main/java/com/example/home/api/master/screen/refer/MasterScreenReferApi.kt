package com.example.home.api.master.screen.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.master.screen.refer.request.MasterScreenReferRequest
import com.example.home.api.master.screen.refer.response.MasterScreenReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.master.ScreenId
import com.example.home.service.master.screen.MasterScreenService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MasterScreenReferApi(
    private val service: MasterScreenService

) {
    companion object {
        const val API_PATH = "api/master/screen/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: MasterScreenReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestId = if (request.screenId == null) null else ScreenId(request.screenId)

        val serviceExecResult = service.refer(
            requestId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "画面情報参照失敗"
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
        val dataObject = serviceExecResult.masterScreenList?.map { screen ->
            MasterScreenReferResponse.ScreenObject(
                screen.screenId.value,
                screen.screenName.value,
                screen.screenRemarks.value,
            )
        }

        val screenResponse =
            MasterScreenReferResponse(
                status,
                message,
                MasterScreenReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(screenResponse)
    }
}