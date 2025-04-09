package com.example.home.api.template.input.update


import com.example.home.api.ErrorResponse
import com.example.home.api.template.input.update.request.ShoppingInputUpdateRequest
import com.example.home.api.template.input.update.response.ShoppingInputUpdateResponse
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
import com.example.home.service.template.ShoppingInputTemplateService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateInputUpdateApi(
    private val shoppingInputTemplateService: ShoppingInputTemplateService

) {
    companion object {
        const val API_PATH = "api/template/input/update"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingInputUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestTemplateId = TemplateId(request.templateId)

        val requestTemplateName =
            if (request.templateName == null) null else request.templateName.let { TemplateName(it) }
        val requestMemberId = if (request.memberId == 0) null else request.memberId?.let { MemberId(it) }
        val requestCategoryId = if (request.categoryId == 0) null else request.categoryId?.let { CategoryId(it) }
        val requestType = if (request.type == 0) null else request.type?.let { ShoppingType(it) }
        val requestPayment = if (request.payment == 0) null else request.payment?.let { ShoppingPayment(it) }
        val requestSettlement =
            if (request.settlement == 0) null else request.settlement?.let { ShoppingSettlement(it) }
        val requestAmount = if (request.amount == 0) null else request.amount?.let { Amount(it) }
        val requestRemarks = if (request.remarks == null) null else request.remarks.let { ShoppingRemarks(it) }
        val requestUseFlg = if (request.use == 0) null else request.use?.let { TemplateUseFlg(it) }

        val serviceExecResult = shoppingInputTemplateService.update(
            requestGroupsId,
            requestTemplateId,
            requestTemplateName,
            requestMemberId,
            requestCategoryId,
            requestType,
            requestPayment,
            requestSettlement,
            requestAmount,
            requestRemarks,
            requestUseFlg
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "入力用テンプレート更新失敗"
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
            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.データ不在エラー.message
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

        val shoppingInputUpdateResponse =
            ShoppingInputUpdateResponse(
                status,
                message,
                ShoppingInputUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingInputUpdateResponse)
    }
}