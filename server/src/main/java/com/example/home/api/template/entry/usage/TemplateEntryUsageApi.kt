package com.example.home.api.template.entry.usage


import com.example.home.api.ErrorResponse
import com.example.home.api.template.entry.usage.request.ShoppingEntryUsageRequest
import com.example.home.api.template.entry.usage.response.ShoppingEntryUsageResponse
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
class TemplateEntryUsageApi(
    private val shoppingEntryTemplateService: ShoppingEntryTemplateService

) {
    companion object {
        const val API_PATH = "api/template/entry/usage"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingEntryUsageRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestTemplateId = TemplateId(request.templateId)

        val serviceExecResult = shoppingEntryTemplateService.usage(
            requestGroupsId,
            requestTemplateId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "登録用テンプレート有効化失敗"
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
        val dataObject = serviceExecResult.updateRows

        val shoppingEntryUsageResponse =
            ShoppingEntryUsageResponse(
                status,
                message,
                ShoppingEntryUsageResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingEntryUsageResponse)
    }
}