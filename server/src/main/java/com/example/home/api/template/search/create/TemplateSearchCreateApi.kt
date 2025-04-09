package com.example.home.api.template.search.create


import com.example.home.api.ErrorResponse
import com.example.home.api.template.search.create.request.ShoppingSearchCreateRequest
import com.example.home.api.template.search.create.response.ShoppingSearchCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.domain.value_object.template.TemplateName
import com.example.home.domain.value_object.template.TemplateUseFlg
import com.example.home.service.template.ShoppingSearchTemplateService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateSearchCreateApi(
    private val shoppingSearchTemplateService: ShoppingSearchTemplateService

) {
    companion object {
        const val API_PATH = "api/template/search/create"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingSearchCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestTemplateId = TemplateId(request.templateId)
        val requestTemplateName = TemplateName(request.templateName)
        val requestMemberId = MemberId(request.memberId)
        val requestCategoryId = CategoryId(request.categoryId)
        val requestType = ShoppingType(request.type)
        val requestPayment = ShoppingPayment(request.payment)
        val requestSettlement = ShoppingSettlement(request.settlement)
        val requestMinAmount = Amount(request.minAmount)
        val requestMaxAmount = Amount(request.maxAmount)
        val requestRemarks = ShoppingRemarks(request.remarks)
        val requestUseFlg = TemplateUseFlg(request.use)

        val serviceExecResult = shoppingSearchTemplateService.save(
            requestGroupsId,
            requestTemplateId,
            requestTemplateName,
            requestMemberId,
            requestCategoryId,
            requestType,
            requestPayment,
            requestSettlement,
            requestMinAmount,
            requestMaxAmount,
            requestRemarks,
            requestUseFlg
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "検索用テンプレート登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.バリデーションエラー.message
            }
            if (serviceExecResult.result == ResponseCode.重複するテンプレートID.code) {
                parameter = "-"
                errorMessage = ResponseCode.重複するテンプレートID.message
            }
            if (serviceExecResult.result == ResponseCode.存在しないメンバー.code) {
                parameter = "-"
                errorMessage = ResponseCode.存在しないメンバー.message
            }
            if (serviceExecResult.result == ResponseCode.存在しないカテゴリー.code) {
                parameter = "-"
                errorMessage = ResponseCode.存在しないカテゴリー.message
            }
            if (serviceExecResult.result == ResponseCode.存在しない購入種別.code) {
                parameter = "-"
                errorMessage = ResponseCode.存在しない購入種別.message
            }
            if (serviceExecResult.result == ResponseCode.存在しない支払い方法.code) {
                parameter = "-"
                errorMessage = ResponseCode.存在しない支払い方法.message
            }
            if (serviceExecResult.result == ResponseCode.存在しない精算状況.code) {
                parameter = "-"
                errorMessage = ResponseCode.存在しない精算状況.message
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
        val dataObject = ShoppingSearchCreateResponse.TemplateObject(
            serviceExecResult.shoppingSearchTemplate?.id?.value,
            serviceExecResult.shoppingSearchTemplate?.groupsId?.value,
            serviceExecResult.shoppingSearchTemplate?.templateId?.value,
            serviceExecResult.shoppingSearchTemplate?.templateName?.value,
            serviceExecResult.shoppingSearchTemplate?.memberId?.value,
            serviceExecResult.shoppingSearchTemplate?.categoryId?.value,
            serviceExecResult.shoppingSearchTemplate?.shoppingType?.value,
            serviceExecResult.shoppingSearchTemplate?.shoppingPayment?.value,
            serviceExecResult.shoppingSearchTemplate?.shoppingSettlement?.value,
            serviceExecResult.shoppingSearchTemplate?.shoppingMinAmount?.value,
            serviceExecResult.shoppingSearchTemplate?.shoppingMaxAmount?.value,
            serviceExecResult.shoppingSearchTemplate?.shoppingRemarks?.value,
            serviceExecResult.shoppingSearchTemplate?.templateUseFlg?.value,
            serviceExecResult.shoppingSearchTemplate?.templateDeletedFlg?.value
        )

        val shoppingSearchCreateResponse =
            ShoppingSearchCreateResponse(
                status,
                message,
                ShoppingSearchCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingSearchCreateResponse)
    }
}