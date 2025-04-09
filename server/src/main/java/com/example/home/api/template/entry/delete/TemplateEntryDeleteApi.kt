package com.example.home.api.template.entry.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.template.entry.delete.request.ShoppingEntryDeleteRequest
import com.example.home.api.template.entry.delete.response.ShoppingEntryDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.service.template.ShoppingEntryTemplateService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateEntryDeleteApi(
    private val shoppingEntryTemplateService: ShoppingEntryTemplateService

) {
    companion object {
        const val API_PATH = "api/template/entry/delete"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingEntryDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestTemplateId = if (request.templateId == null) null else request.templateId.let { TemplateId(it) }

        val serviceExecResult = shoppingEntryTemplateService.delete(
            requestGroupsId,
            requestTemplateId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "登録用テンプレート削除失敗"
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

        val shoppingEntryDeleteResponse =
            ShoppingEntryDeleteResponse(
                status,
                message,
                ShoppingEntryDeleteResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingEntryDeleteResponse)
    }
}