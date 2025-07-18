package com.example.home.api.master.screen.create


import com.example.home.api.ErrorResponse
import com.example.home.api.master.screen.create.request.MasterScreenCreateRequest
import com.example.home.api.master.screen.refer.response.MasterScreenCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.master.ScreenId
import com.example.home.domain.value_object.master.ScreenName
import com.example.home.domain.value_object.master.ScreenRemarks
import com.example.home.service.master.screen.MasterScreenService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MasterScreenCreateApi(
    private val service: MasterScreenService

) {
    companion object {
        const val API_PATH = "api/master/screen/create"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: MasterScreenCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestId = ScreenId(request.screenId)
        val requestName = ScreenName(request.screenName)
        val requestRemarks = ScreenRemarks(request.remarks)

        val serviceExecResult = service.save(
            requestId, requestName, requestRemarks
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "画面情報登録失敗"
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
        val dataObject = MasterScreenCreateResponse.ScreenObject(
            serviceExecResult.masterScreen.screenId.value,
            serviceExecResult.masterScreen.screenName.value,
            serviceExecResult.masterScreen.screenRemarks.value
        )

        val screenResponse =
            MasterScreenCreateResponse(
                status,
                message,
                MasterScreenCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(screenResponse)
    }
}