package com.example.home.api.template.search.update


import com.example.home.api.ErrorResponse
import com.example.home.api.template.search.update.request.ShoppingSearchUpdateRequest
import com.example.home.api.template.search.update.response.ShoppingSearchUpdateResponse
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
import com.example.home.domain.value_object.template.TemplateNo
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
class TemplateSearchUpdateApi(
    private val shoppingSearchTemplateService: ShoppingSearchTemplateService

) {
    companion object {
        const val API_PATH = "api/template/search/update"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingSearchUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestTemplateNo = TemplateNo(request.templateNo)
        val requestTemplateId = TemplateId(request.templateId)
        val requestTemplateName =TemplateName(request.templateName)

        val requestMemberId = request.memberId?.let { MemberId(it) }
        val requestCategoryId = request.categoryId?.let { CategoryId(it) }
        val requestType = request.type?.let { ShoppingType(it) }
        val requestPayment = request.payment?.let { ShoppingPayment(it) }
        val requestSettlement = request.settlement?.let { ShoppingSettlement(it) }
        val requestMinAmount = request.minAmount?.let { Amount(it) }
        val requestMaxAmount = request.maxAmount?.let { Amount(it) }
        val requestRemarks = request.remarks?.let { ShoppingRemarks(it) }
        val requestUseFlg = TemplateUseFlg(request.use)

        val serviceExecResult = shoppingSearchTemplateService.update(
            requestGroupsId,
            requestTemplateNo,
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
            val message = "検索用テンプレート更新失敗"
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
            if (serviceExecResult.result == ResponseCode.最小金額が最大金額より高い.code) {
                parameter = "-"
                errorMessage = ResponseCode.最小金額が最大金額より高い.message
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
        val dataObject = serviceExecResult.updateRows

        val shoppingSearchUpdateResponse =
            ShoppingSearchUpdateResponse(
                status,
                message,
                ShoppingSearchUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingSearchUpdateResponse)
    }
}