package com.example.home.api.template.entry.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.template.entry.refer.request.ShoppingEntryReferRequest
import com.example.home.api.template.entry.refer.response.ShoppingEntryReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.service.template.ShoppingEntryTemplateService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateEntryReferApi(
    private val shoppingEntryTemplateService: ShoppingEntryTemplateService

) {
    companion object {
        const val API_PATH = "api/template/entry/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingEntryReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestTemplateId = if (request.templateId == "") null else request.templateId?.let { TemplateId(it) }

        val serviceExecResult = shoppingEntryTemplateService.refer(
            requestGroupsId, requestTemplateId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "登録用テンプレート参照失敗"
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
        val dataObject = serviceExecResult.shoppingEntryTemplateList?.map { template ->
            ShoppingEntryReferResponse.TemplateObject(
                template.id.value,
                template.groupsId.value,
                template.templateNo.value.toString(),
                template.templateId.value,
                template.templateName.value,
                template.memberId.value,
                template.categoryId.value,
                template.shoppingType.value,
                template.shoppingPayment.value,
                template.shoppingSettlement.value,
                template.shoppingAmount.value,
                template.shoppingRemarks.value,
                template.templateUseFlg.value,
                template.templateDeletedFlg.value,
            )
        }

        val shoppingEntryReferResponse =
            ShoppingEntryReferResponse(
                status,
                message,
                ShoppingEntryReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingEntryReferResponse)
    }
}