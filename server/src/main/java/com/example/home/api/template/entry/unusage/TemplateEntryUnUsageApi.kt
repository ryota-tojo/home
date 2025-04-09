package com.example.home.api.template.entry.unusage


import com.example.home.api.ErrorResponse
import com.example.home.api.template.entry.unusage.request.ShoppingEntryUnUsageRequest
import com.example.home.api.template.entry.unusage.response.ShoppingEntryUnUsageResponse
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
class TemplateEntryUnUsageApi(
    private val shoppingEntryTemplateService: ShoppingEntryTemplateService

) {
    companion object {
        const val API_PATH = "api/template/entry/un_usage"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingEntryUnUsageRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestTemplateId = TemplateId(request.templateId)

        val serviceExecResult = shoppingEntryTemplateService.unUsage(
            requestGroupsId,
            requestTemplateId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "登録用テンプレート無効化失敗"
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

        val shoppingEntryUnUsageResponse =
            ShoppingEntryUnUsageResponse(
                status,
                message,
                ShoppingEntryUnUsageResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingEntryUnUsageResponse)
    }
}