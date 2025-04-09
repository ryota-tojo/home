package com.example.home.api.shopping.duplication_check


import com.example.home.api.shopping.duplication_check.request.ShoppingDuplicationCheckRequest
import com.example.home.api.shopping.duplication_check.response.ShoppingDuplicationCheckResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
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
class ShoppingDuplicationCheckApi(
    private val shoppingService: ShoppingService

) {
    companion object {
        const val API_PATH = "api/shopping/duplication_check"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingDuplicationCheckRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestShoppingDate = LocalDate.parse((request.shoppingDate))
        val requestMemberId = MemberId(request.memberId)
        val requestCategoryId = CategoryId(request.categoryId)
        val requestType = ShoppingType(request.type)
        val requestPayment = ShoppingPayment(request.payment)
        val requestSettlement = ShoppingSettlement(request.settlement)
        val requestAmount = Amount(request.amount)
        val requestRemarks = ShoppingRemarks(request.remarks)

        val serviceExecResult = shoppingService.isDuplication(
            requestGroupsId,
            requestShoppingDate,
            requestMemberId,
            requestCategoryId,
            requestType,
            requestPayment,
            requestSettlement,
            requestAmount,
            requestRemarks,
        )

        // 成功時のレスポンス
        val status = "success"
        val message = ResponseCode.成功.message
        val dataObject = if (serviceExecResult) {
            1
        } else {
            0
        }

        val shoppingDuplicationCheckResponse =
            ShoppingDuplicationCheckResponse(
                status,
                message,
                ShoppingDuplicationCheckResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingDuplicationCheckResponse)
    }
}