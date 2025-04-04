package com.example.home.api.budgets.update


import com.example.home.api.ErrorResponse
import com.example.home.api.budgets.update.request.BudgetsUpdateRequest
import com.example.home.api.budgets.update.response.BudgetsUpdateResponse
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
class BudgetsUpdateApi(
    private val budgetsService: BudgetsService

) {
    companion object {
        const val API_PATH = "api/budgets/update"
    }

    @PostMapping(value = [API_PATH])
    fun update(
        @RequestBody @Valid request: BudgetsUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = YYYY(request.yyyy)
        val requestMM = MM(request.mm)
        val requestCategoryId = CategoryId(request.categoryId)
        val requestAmount = if (request.amount == 0) null else request.amount?.let { Amount(it) }

        val serviceExecResult = budgetsService.update(
            requestGroupsId, requestYYYY, requestMM, requestCategoryId, requestAmount
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "予算更新失敗"
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
        val dataObject = serviceExecResult.updateRows

        val budgetsUpdateResponse =
            BudgetsUpdateResponse(
                status,
                message,
                BudgetsUpdateResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(budgetsUpdateResponse)
    }
}