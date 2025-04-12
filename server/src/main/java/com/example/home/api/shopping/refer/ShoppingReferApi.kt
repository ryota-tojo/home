package com.example.home.api.shopping.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.shopping.refer.request.ShoppingReferRequest
import com.example.home.api.shopping.refer.response.ShoppingReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.*
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.shopping.ShoppingService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class ShoppingReferApi(
    private val shoppingService: ShoppingService

) {
    companion object {
        const val API_PATH = "api/shopping/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestShoppingId = request.shoppingId?.let { ShoppingId(it) }
        val requestGroupsId = request.groupsId?.takeIf { it.isNotBlank() }?.let { GroupsId(it) }
        val requestUserId = request.userId?.let { UserId(it) }
        val requestShoppingDate = request.shoppingDate?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it) }
        val requestYYYY = requestShoppingDate?.year?.let { YYYY(it) }
        val requestMM = requestShoppingDate?.month?.let { MM(it.value) }
        val requestMemberId = request.memberId?.let { MemberId(it) }
        val requestCategoryId = request.categoryId?.let { CategoryId(it) }
        val requestType = request.type?.let { ShoppingType(it) }
        val requestPayment = request.payment?.let { ShoppingPayment(it) }
        val requestSettlement = request.settlement?.let { ShoppingSettlement(it) }
        val requestMinAmount = request.minAmount?.let { Amount(it) }
        val requestMaxAmount = request.maxAmount?.let { Amount(it) }
        val requestRemarks = request.remarks?.takeIf { it.isNotBlank() }?.let { ShoppingRemarks(it) }

        val requestOffset = request.offSet
        val requestLimit = request.limit

        val serviceExecResult = shoppingService.refer(
            requestShoppingId,
            requestGroupsId,
            requestUserId,
            requestYYYY,
            requestMM,
            requestMemberId,
            requestCategoryId,
            requestType,
            requestPayment,
            requestSettlement,
            requestMinAmount,
            requestMaxAmount,
            requestRemarks,
            requestOffset,
            requestLimit
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "購入データ参照失敗"
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
        val dataObject = serviceExecResult.shoppingList?.map { shopping ->
            ShoppingReferResponse.ShoppingObject(
                shopping.id.value,
                shopping.groupsId.value,
                shopping.userId.value,
                shopping.shoppingDate.toString(),
                shopping.memberId.value,
                shopping.categoryId.value,
                shopping.shoppingType.value,
                shopping.shoppingPayment.value,
                shopping.shoppingSettlement.value,
                shopping.shoppingAmount.value,
                shopping.shoppingRemarks.value,
                shopping.fixedFlg.value,
            )
        }

        val shoppingReferResponse =
            ShoppingReferResponse(
                status,
                message,
                ShoppingReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingReferResponse)
    }
}