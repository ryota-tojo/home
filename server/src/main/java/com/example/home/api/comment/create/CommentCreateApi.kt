package com.example.home.api.comment.create


import com.example.home.api.ErrorResponse
import com.example.home.api.comment.create.request.CommentCreateRequest
import com.example.home.api.comment.create.response.CommentCreateResponse
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
class CommentCreateApi(
    private val commentService: CommentService

) {
    companion object {
        const val API_PATH = "api/comment/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: CommentCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = YYYY(request.yyyy)
        val requestMM = MM(request.mm)
        val requestContent = Content(request.content)

        val serviceExecResult = commentService.save(
            requestGroupsId, requestYYYY, requestMM, requestContent
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "コメント登録失敗"
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
        val dataObject = CommentCreateResponse.CommentObject(
            serviceExecResult.comment?.id,
            serviceExecResult.comment?.groupsId?.value,
            serviceExecResult.comment?.yyyy?.value,
            serviceExecResult.comment?.mm?.value,
            serviceExecResult.comment?.content?.value,
            serviceExecResult.comment?.fixedFlg?.value,

            )

        val categoryCreateResponse =
            CommentCreateResponse(
                status,
                message,
                CommentCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(categoryCreateResponse)
    }
}