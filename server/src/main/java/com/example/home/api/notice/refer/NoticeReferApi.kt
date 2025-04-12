package com.example.home.api.notice.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.notice.refer.request.NoticeReferRequest
import com.example.home.api.notice.refer.response.NoticeReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.notice.NoticeId
import com.example.home.service.notice.NoticeService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class NoticeReferApi(
    private val noticeService: NoticeService

) {
    companion object {
        const val API_PATH = "api/notice/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: NoticeReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestId = if (request.noticeId == 0) null else request.noticeId.let { NoticeId(it) }

        val requestOffset = request.offSet
        val requestLimit = request.limit

        val serviceExecResult = noticeService.refer(
            requestId, requestOffset, requestLimit
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "お知らせ参照失敗"
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
        val dataObject = serviceExecResult.noticeList?.map { notice ->
            NoticeReferResponse.NoticeObject(
                notice.noticeId.value,
                notice.noticeTitle.value,
                notice.noticeContent.value,
                parseLocalDateTime(notice.createDate.toString()),
                parseLocalDateTime(notice.updateDate.toString()),
            )
        }

        val noticeReferResponse =
            NoticeReferResponse(
                status,
                message,
                NoticeReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(noticeReferResponse)
    }
}