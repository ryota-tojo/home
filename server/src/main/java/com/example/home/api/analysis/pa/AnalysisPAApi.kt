package com.example.home.api.analysis.pa


import com.example.home.api.ErrorResponse
import com.example.home.api.analysis.pa.request.AnalysisPARequest
import com.example.home.api.analysis.pa.response.AnalysisPAResponse
import com.example.home.api.analysis.pa.response.AnalysisPAResponse.*
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.analysis.AnalysisPAService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AnalysisPAApi(
    private val analysisPAService: AnalysisPAService

) {
    companion object {
        const val API_PATH = "api/analysis/p_a"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: AnalysisPARequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = YYYY(request.yyyy)

        val serviceExecResult = analysisPAService.analysisPA(
            requestGroupsId, requestYYYY
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "分析データ（予実対比）取得失敗"
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
        val analysisPATotalData = serviceExecResult.analysisPA?.analysisPATotal?.analysisPATotalData
        val dataObject = DataObject(
            serviceExecResult.analysisPA?.analysisPACategoryList?.map { category ->
                CategoryObject(
                    category.categoryId.value,
                    category.categoryName.value,
                    category.analysisPAMonthData.map { analysisPAMonth ->
                        MonthDataObject(
                            analysisPAMonth.expenseAmount.value,
                            analysisPAMonth.incomeAndBudgetsAmount.value,
                            analysisPAMonth.averageAmount.value,
                            analysisPAMonth.useRate.value,
                            analysisPAMonth.balanceAmount.value,
                        )
                    },
                    YearDataObject(
                        category.analysisPAYearData.expenseAmount.value,
                        category.analysisPAYearData.incomeAndBudgetsAmount.value,
                        category.analysisPAYearData.averageAmount.value,
                        category.analysisPAYearData.useRate.value,
                        category.analysisPAYearData.balanceAmount.value,
                    )
                )
            },
            TotalObject(
                analysisPATotalData?.expenseAmount?.value,
                analysisPATotalData?.incomeAndBudgetsAmount?.value,
                analysisPATotalData?.averageAmount?.value,
                analysisPATotalData?.useRate?.value,
                analysisPATotalData?.balanceAmount?.value,

                )
        )

        val analysisPAResponse =
            AnalysisPAResponse(
                status,
                message,
                dataObject
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(analysisPAResponse)
    }
}