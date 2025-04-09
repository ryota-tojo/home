package com.example.home.api.shopping.create


import com.example.home.api.ErrorResponse
import com.example.home.api.shopping.create.request.ShoppingCreateRequest
import com.example.home.api.shopping.create.response.ShoppingCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
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
class ShoppingCreateApi(
    private val shoppingService: ShoppingService

) {
    companion object {
        const val API_PATH = "api/shopping/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: ShoppingCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestUserId = UserId(request.userId)
        val requestShoppingDate = LocalDate.parse(request.shoppingDate)
        val requestMemberId = MemberId(request.memberId)
        val requestCategoryId = CategoryId(request.categoryId)
        val requestType = ShoppingType(request.type)
        val requestPayment = ShoppingPayment(request.payment)
        val requestSettlement = ShoppingSettlement(request.settlement)
        val requestAmount = Amount(request.amount)
        val requestRemarks = ShoppingRemarks(request.remarks)

        val serviceExecResult = shoppingService.save(
            requestGroupsId,
            requestUserId,
            requestShoppingDate,
            requestMemberId,
            requestCategoryId,
            requestType,
            requestPayment,
            requestSettlement,
            requestAmount,
            requestRemarks
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "購入データ登録失敗"
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
        val dataObject = ShoppingCreateResponse.ShoppingObject(
            serviceExecResult.shopping?.id?.value,
            serviceExecResult.shopping?.groupsId?.value,
            serviceExecResult.shopping?.userId?.value,
            serviceExecResult.shopping?.shoppingDate.toString(),
            serviceExecResult.shopping?.memberId?.value,
            serviceExecResult.shopping?.categoryId?.value,
            serviceExecResult.shopping?.shoppingType?.value,
            serviceExecResult.shopping?.shoppingPayment?.value,
            serviceExecResult.shopping?.shoppingSettlement?.value,
            serviceExecResult.shopping?.shoppingAmount?.value,
            serviceExecResult.shopping?.shoppingRemarks?.value,
            serviceExecResult.shopping?.fixedFlg?.value,
        )

        val shoppingCreateResponse =
            ShoppingCreateResponse(
                status,
                message,
                ShoppingCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingCreateResponse)
    }
}