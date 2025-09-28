package com.example.home.api.template.entry.update


import com.example.home.api.ErrorResponse
import com.example.home.api.template.entry.update.request.ShoppingEntryUpdateRequest
import com.example.home.api.template.entry.update.response.ShoppingEntryUpdateResponse
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
import com.example.home.service.template.ShoppingEntryTemplateService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateEntryUpdateApi(
    private val shoppingEntryTemplateService: ShoppingEntryTemplateService

) {
    companion object {
        const val API_PATH = "api/template/entry/update"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingEntryUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestTemplateNo = if (request.templateNo == null) null else request.templateNo.let { TemplateNo(it) }
        val requestTemplateId = TemplateId(request.templateId)

        val requestTemplateName =
            if (request.templateName == null) null else request.templateName.let { TemplateName(it) }
        val requestMemberId = if (request.memberId == 0) null else request.memberId?.let { MemberId(it) }
        val requestCategoryId = if (request.categoryId == 0) null else request.categoryId?.let { CategoryId(it) }
        val requestType = if (request.type == null) null else request.type.let { ShoppingType(it) }
        val requestPayment = if (request.payment == null) null else request.payment.let { ShoppingPayment(it) }
        val requestSettlement =
            if (request.settlement == null) null else request.settlement.let { ShoppingSettlement(it) }
        val requestAmount = if (request.amount == null) null else request.amount.let { Amount(it) }
        val requestRemarks = if (request.remarks == null) null else request.remarks.let { ShoppingRemarks(it) }
        val requestUseFlg = if (request.use == null) null else request.use.let { TemplateUseFlg(it) }

        val serviceExecResult = shoppingEntryTemplateService.update(
            requestGroupsId,
            requestTemplateNo,
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
            val message = "登録用テンプレート更新失敗"
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

            println(serviceExecResult.result)

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

        val shoppingEntryUpdateResponse =
            ShoppingEntryUpdateResponse(
                status,
                message,
                ShoppingEntryUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingEntryUpdateResponse)
    }
}