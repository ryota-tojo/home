package com.example.home.api.budgets.create


import com.example.home.api.ErrorResponse
import com.example.home.api.budgets.create.request.BudgetsCreateRequest
import com.example.home.api.budgets.create.response.BudgetsCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.budgets.BudgetsService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BudgetsCreateApi(
    private val budgetsService: BudgetsService

) {
    companion object {
        const val API_PATH = "api/budgets/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: BudgetsCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = YYYY(request.yyyy)
        val requestMM = MM(request.mm)
        val requestCategoryId = CategoryId(request.categoryId)
        val requestAmount = Amount(request.amount)

        val serviceExecResult = budgetsService.save(
            requestGroupsId, requestYYYY, requestMM, requestCategoryId, requestAmount
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "予算登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.重複エラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.重複エラー.message
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
        val dataObject = BudgetsCreateResponse.BudgetsObject(
            serviceExecResult.budgets?.groupsId?.value,
            serviceExecResult.budgets?.YYYY?.value.toString(),
            serviceExecResult.budgets?.MM?.value.toString(),
            serviceExecResult.budgets?.categoryId?.value.toString(),
            serviceExecResult.budgets?.amount?.value.toString(),
        )

        val budgetsCreateResponse =
            BudgetsCreateResponse(
                status,
                message,
                BudgetsCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(budgetsCreateResponse)
    }
}