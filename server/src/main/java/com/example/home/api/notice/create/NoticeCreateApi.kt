package com.example.home.api.notice.create


import com.example.home.api.ErrorResponse
import com.example.home.api.notice.create.request.NoticeCreateRequest
import com.example.home.api.notice.create.response.NoticeCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.notice.NoticeContent
import com.example.home.domain.value_object.notice.NoticeTitle
import com.example.home.service.notice.NoticeService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class NoticeCreateApi(
    private val noticeService: NoticeService

) {
    companion object {
        const val API_PATH = "api/notice/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: NoticeCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestTitle = NoticeTitle(request.title)
        val requestContent = NoticeContent(request.content)

        val serviceExecResult = noticeService.save(
            requestTitle, requestContent
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "お知らせ登録失敗"
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
        val dataObject = NoticeCreateResponse.NoticeObject(
            serviceExecResult.notice?.noticeId?.value,
            serviceExecResult.notice?.noticeTitle?.value,
            serviceExecResult.notice?.noticeContent?.value,
            parseLocalDateTime(serviceExecResult.notice?.createDate?.toString()),
            parseLocalDateTime(serviceExecResult.notice?.updateDate?.toString()),
        )

        val noticeCreateResponse =
            NoticeCreateResponse(
                status,
                message,
                NoticeCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(noticeCreateResponse)
    }
}