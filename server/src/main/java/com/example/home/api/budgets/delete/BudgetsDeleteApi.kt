package com.example.home.api.budgets.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.budgets.delete.request.BudgetsDeleteRequest
import com.example.home.api.budgets.delete.response.BudgetsDeleteResponse
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
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BudgetsDeleteApi(
    private val budgetsService: BudgetsService

) {
    companion object {
        const val API_PATH = "api/budgets/delete"
    }

    @PostMapping(value = [API_PATH])
    fun delete(
        @RequestBody @Valid request: BudgetsDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = if (request.yyyy == 0) null else request.yyyy?.let { YYYY(it) }
        val requestMM = if (request.mm == 0) null else request.mm?.let { MM(it) }
        val requestCategoryId = if (request.categoryId == 0) null else request.categoryId?.let { CategoryId(it) }

        val serviceExecResult = budgetsService.delete(
            requestGroupsId, requestYYYY, requestMM, requestCategoryId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "予算削除失敗"
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
        val dataObject = serviceExecResult.deleteRows

        val budgetsDeleteResponse =
            BudgetsDeleteResponse(
                status,
                message,
                BudgetsDeleteResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(budgetsDeleteResponse)
    }
}