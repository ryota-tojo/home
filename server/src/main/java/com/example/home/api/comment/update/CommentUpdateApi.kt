package com.example.home.api.comment.update


import com.example.home.api.ErrorResponse
import com.example.home.api.comment.update.request.CommentUpdateRequest
import com.example.home.api.comment.update.response.CommentUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.comment.Content
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
class CommentUpdateApi(
    private val commentService: CommentService

) {
    companion object {
        const val API_PATH = "api/comment/update"
    }

    @PostMapping(value = [API_PATH])
    fun update(
        @RequestBody @Valid request: CommentUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = YYYY(request.yyyy)
        val requestMM = MM(request.mm)
        val requestContent = Content(request.content)


        val serviceExecResult = commentService.update(
            requestGroupsId, requestYYYY, requestMM, requestContent
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "コメント更新失敗"
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
        val dataObject = serviceExecResult.updateRows

        val commentUpdateResponse =
            CommentUpdateResponse(
                status,
                message,
                CommentUpdateResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(commentUpdateResponse)
    }
}