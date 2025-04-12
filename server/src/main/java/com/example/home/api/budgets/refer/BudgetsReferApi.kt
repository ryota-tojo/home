package com.example.home.api.budgets.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.budgets.refer.request.BudgetsReferRequest
import com.example.home.api.budgets.refer.response.BudgetsReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.budgets.BudgetsService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BudgetsReferApi(
    private val budgetsService: BudgetsService

) {
    companion object {
        const val API_PATH = "api/budgets/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: BudgetsReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = if (request.yyyy == 0) null else request.yyyy?.let { YYYY(it) }
        val requestMM = if (request.mm == 0) null else request.mm?.let { MM(it) }
        val requestCategoryId = if (request.categoryId == 0) null else request.categoryId?.let { CategoryId(it) }

        val serviceExecResult = budgetsService.refer(
            requestGroupsId, requestYYYY, requestMM, requestCategoryId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "予算参照失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.データ不在エラー.message
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
        val dataObject = serviceExecResult.budgets?.map { budget ->
            BudgetsReferResponse.BudgetsObject(
                budget.groupsId.value,
                budget.YYYY.value.toString(),
                budget.MM.value.toString(),
                budget.categoryId.value.toString(),
                budget.amount.value.toString(),
            )
        }

        val budgetsReferResponse =
            BudgetsReferResponse(
                status,
                message,
                BudgetsReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(budgetsReferResponse)
    }
}