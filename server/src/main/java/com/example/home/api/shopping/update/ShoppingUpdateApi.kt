package com.example.home.api.shopping.update


import com.example.home.api.ErrorResponse
import com.example.home.api.shopping.update.request.ShoppingUpdateRequest
import com.example.home.api.shopping.update.response.ShoppingUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.*
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.shopping.ShoppingService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class ShoppingUpdateApi(
    private val shoppingService: ShoppingService

) {
    companion object {
        const val API_PATH = "api/shopping/update"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestShoppingId = ShoppingId(request.shoppingId)
        val requestGroupsId = GroupsId(request.groupsId)
        val requestUserId = request.userId?.let { UserId(it) }
        val requestShoppingDate = request.shoppingDate?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) }
        val requestMemberId = request.memberId?.let { MemberId(it) }
        val requestCategoryId = request.categoryId?.let { CategoryId(it) }
        val requestType = request.type?.let { ShoppingType(it) }
        val requestPayment = request.payment?.let { ShoppingPayment(it) }
        val requestSettlement = request.settlement?.let { ShoppingSettlement(it) }
        val requestAmount = request.amount?.let { Amount(it) }
        val requestRemarks = request.remarks?.takeIf { it.isNotBlank() }?.let { ShoppingRemarks(it) }

        val serviceExecResult = shoppingService.update(
            requestShoppingId,
            requestGroupsId,
            requestUserId,
            requestShoppingDate,
            requestMemberId,
            requestCategoryId,
            requestType,
            requestPayment,
            requestSettlement,
            requestAmount,
            requestRemarks,
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "購入データ更新失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

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

        val shoppingUpdateResponse =
            ShoppingUpdateResponse(
                status,
                message,
                ShoppingUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingUpdateResponse)
    }
}