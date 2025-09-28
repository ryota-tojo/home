package com.example.home.api.template.input.create


import com.example.home.api.ErrorResponse
import com.example.home.api.template.input.create.request.ShoppingInputCreateRequest
import com.example.home.api.template.input.create.response.ShoppingInputCreateResponse
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
import com.example.home.service.template.ShoppingInputTemplateService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TemplateInputCreateApi(
    private val shoppingInputTemplateService: ShoppingInputTemplateService

) {
    companion object {
        const val API_PATH = "api/template/input/create"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingInputCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestTemplateNo = TemplateNo(request.templateNo)
        val requestTemplateId = TemplateId(request.templateId)
        val requestTemplateName = TemplateName(request.templateName)
        val requestMemberId = MemberId(request.memberId)
        val requestCategoryId = CategoryId(request.categoryId)
        val requestType = ShoppingType(request.type)
        val requestPayment = ShoppingPayment(request.payment)
        val requestSettlement = ShoppingSettlement(request.settlement)
        val requestAmount = Amount(request.amount)
        val requestRemarks = ShoppingRemarks(request.remarks)
        val requestUseFlg = TemplateUseFlg(request.use)

        val serviceExecResult = shoppingInputTemplateService.save(
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
            val message = "入力用テンプレート登録失敗"
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
        val dataObject = ShoppingInputCreateResponse.TemplateObject(
            serviceExecResult.shoppingInputTemplate?.id?.value,
            serviceExecResult.shoppingInputTemplate?.groupsId?.value,
            serviceExecResult.shoppingInputTemplate?.templateNo?.value.toString(),
            serviceExecResult.shoppingInputTemplate?.templateId?.value,
            serviceExecResult.shoppingInputTemplate?.templateName?.value,
            serviceExecResult.shoppingInputTemplate?.memberId?.value,
            serviceExecResult.shoppingInputTemplate?.categoryId?.value,
            serviceExecResult.shoppingInputTemplate?.shoppingType?.value,
            serviceExecResult.shoppingInputTemplate?.shoppingPayment?.value,
            serviceExecResult.shoppingInputTemplate?.shoppingSettlement?.value,
            serviceExecResult.shoppingInputTemplate?.shoppingAmount?.value,
            serviceExecResult.shoppingInputTemplate?.shoppingRemarks?.value,
            serviceExecResult.shoppingInputTemplate?.templateUseFlg?.value,
            serviceExecResult.shoppingInputTemplate?.templateDeletedFlg?.value,
        )

        val shoppingInputCreateResponse =
            ShoppingInputCreateResponse(
                status,
                message,
                ShoppingInputCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingInputCreateResponse)
    }
}