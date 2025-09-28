package com.example.home.api.template.search.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.template.search.refer.request.ShoppingSearchReferRequest
import com.example.home.api.template.search.refer.response.ShoppingSearchReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.service.template.ShoppingSearchTemplateService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateSearchReferApi(
    private val shoppingSearchTemplateService: ShoppingSearchTemplateService

) {
    companion object {
        const val API_PATH = "api/template/search/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingSearchReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestTemplateId = if (request.templateId == "") null else request.templateId?.let { TemplateId(it) }

        val serviceExecResult = shoppingSearchTemplateService.refer(
            requestGroupsId, requestTemplateId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "検索用テンプレート参照失敗"
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
        val dataObject = serviceExecResult.shoppingSearchTemplateList?.map { template ->
            ShoppingSearchReferResponse.TemplateObject(
                template.id.value,
                template.groupsId.value,
                template.templateNo.value.toString(),
                template.templateId.value,
                template.templateName.value,
                template.memberId?.value,
                template.categoryId?.value,
                template.shoppingType?.value,
                template.shoppingPayment?.value,
                template.shoppingSettlement?.value,
                template.shoppingMinAmount?.value,
                template.shoppingMaxAmount?.value,
                template.shoppingRemarks?.value,
                template.templateUseFlg.value,
                template.templateDeletedFlg.value,
            )
        }

        val shoppingSearchReferResponse =
            ShoppingSearchReferResponse(
                status,
                message,
                ShoppingSearchReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingSearchReferResponse)
    }
}