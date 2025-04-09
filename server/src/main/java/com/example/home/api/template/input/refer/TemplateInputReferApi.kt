package com.example.home.api.template.input.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.template.input.refer.request.ShoppingInputReferRequest
import com.example.home.api.template.input.refer.response.ShoppingInputReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.service.template.ShoppingInputTemplateService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateInputReferApi(
    private val shoppingInputTemplateService: ShoppingInputTemplateService

) {
    companion object {
        const val API_PATH = "api/template/input/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingInputReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestTemplateId = if (request.templateId == "") null else request.templateId?.let { TemplateId(it) }

        val serviceExecResult = shoppingInputTemplateService.refer(
            requestGroupsId, requestTemplateId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "入力用テンプレート参照失敗"
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
        val dataObject = serviceExecResult.shoppingInputTemplateList?.map { template ->
            ShoppingInputReferResponse.TemplateObject(
                template.id.value,
                template.groupsId.value,
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

        val shoppingInputReferResponse =
            ShoppingInputReferResponse(
                status,
                message,
                ShoppingInputReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingInputReferResponse)
    }
}