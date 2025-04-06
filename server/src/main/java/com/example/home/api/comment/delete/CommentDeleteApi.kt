package com.example.home.api.comment.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.comment.delete.request.CommentDeleteRequest
import com.example.home.api.comment.delete.response.CommentDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.comment.CommentService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentDeleteApi(
    private val commentService: CommentService

) {
    companion object {
        const val API_PATH = "api/comment/delete"
    }

    @PostMapping(value = [API_PATH])
    fun delete(
        @RequestBody @Valid request: CommentDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = if (request.yyyy == 0) null else request.yyyy?.let { YYYY(it) }
        val requestMM = if (request.mm == 0) null else request.mm?.let { MM(it) }

        val serviceExecResult = commentService.delete(
            requestGroupsId, requestYYYY, requestMM
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "コメント削除失敗"
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
        val dataObject = serviceExecResult.deleteRows

        val commentDeleteResponse =
            CommentDeleteResponse(
                status,
                message,
                CommentDeleteResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(commentDeleteResponse)
    }
}