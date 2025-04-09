package com.example.home.api.notice.update


import com.example.home.api.ErrorResponse
import com.example.home.api.notice.update.request.NoticeUpdateRequest
import com.example.home.api.notice.update.response.NoticeUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.notice.NoticeContent
import com.example.home.domain.value_object.notice.NoticeId
import com.example.home.domain.value_object.notice.NoticeTitle
import com.example.home.service.notice.NoticeService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class NoticeUpdateApi(
    private val noticeService: NoticeService

) {
    companion object {
        const val API_PATH = "api/notice/update"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: NoticeUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestId = NoticeId(request.noticeId)
        val requestTitle = NoticeTitle(request.title)
        val requestContent = NoticeContent(request.content)

        val serviceExecResult = noticeService.update(
            requestId, requestTitle, requestContent
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "お知らせ更新失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "setting_key"
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

        val noticeUpdateResponse =
            NoticeUpdateResponse(
                status,
                message,
                NoticeUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(noticeUpdateResponse)
    }
}