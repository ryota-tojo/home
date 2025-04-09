package com.example.home.api.notice.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.notice.delete.request.NoticeDeleteRequest
import com.example.home.api.notice.delete.response.NoticeDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.notice.NoticeId
import com.example.home.service.notice.NoticeService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class NoticeDeleteApi(
    private val noticeService: NoticeService

) {
    companion object {
        const val API_PATH = "api/notice/delete"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: NoticeDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestId = NoticeId(request.noticeId)

        val serviceExecResult = noticeService.delete(
            requestId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "お知らせ削除失敗"
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
        val dataObject = serviceExecResult.deleteRows

        val noticeDeleteResponse =
            NoticeDeleteResponse(
                status,
                message,
                NoticeDeleteResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(noticeDeleteResponse)
    }
}