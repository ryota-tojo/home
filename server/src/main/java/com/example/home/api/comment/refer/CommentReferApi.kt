package com.example.home.api.comment.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.comment.refer.request.CommentReferRequest
import com.example.home.api.comment.refer.response.CommentReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.comment.CommentService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentReferApi(
    private val commentService: CommentService

) {
    companion object {
        const val API_PATH = "api/comment/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: CommentReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestYYYY = if (request.yyyy == 0) null else request.yyyy?.let { YYYY(it) }
        val requestMM = if (request.mm == 0) null else request.mm?.let { MM(it) }

        val serviceExecResult = commentService.refer(
            requestGroupsId, requestYYYY, requestMM
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "コメント参照失敗"
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
        val dataObject = serviceExecResult.comment?.map { comment ->
            CommentReferResponse.CommentObject(
                comment.id,
                comment.groupsId.value,
                comment.yyyy.value,
                comment.mm.value,
                comment.content.value,
                comment.fixedFlg.value,
            )
        }

        val commentReferResponse =
            CommentReferResponse(
                status,
                message,
                CommentReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(commentReferResponse)
    }
}