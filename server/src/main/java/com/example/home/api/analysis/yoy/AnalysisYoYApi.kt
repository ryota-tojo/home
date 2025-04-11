package com.example.home.api.analysis.yoy


import com.example.home.api.ErrorResponse
import com.example.home.api.analysis.yoy.request.AnalysisYoYRequest
import com.example.home.api.analysis.yoy.response.AnalysisYoYResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.analysis.AnalysisYoYService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AnalysisYoYApi(
    private val analysisYoYService: AnalysisYoYService

) {
    companion object {
        const val API_PATH = "api/analysis/yoy"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: AnalysisYoYRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = YYYY(request.yyyy)

        val serviceExecResult = analysisYoYService.analysisYoY(
            requestGroupsId, requestYYYY
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "分析データ（前年対比）取得失敗"
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
//        val analysisPATotalData = serviceExecResult.analysisPA?.analysisPATotal?.analysisPATotalData
        val dataObject = AnalysisYoYResponse.DataObject(
            serviceExecResult.analysisYoY?.analysisYoYCategoryList?.map { category ->
                AnalysisYoYResponse.CategoryObject(
                    category.categoryId.value,
                    category.categoryName.value,
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[0].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[0].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[0].difference.value,
                        category.analysisYoYCategoryMonthData[0].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[1].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[1].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[1].difference.value,
                        category.analysisYoYCategoryMonthData[1].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[2].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[2].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[2].difference.value,
                        category.analysisYoYCategoryMonthData[2].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[3].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[3].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[3].difference.value,
                        category.analysisYoYCategoryMonthData[3].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[4].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[4].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[4].difference.value,
                        category.analysisYoYCategoryMonthData[4].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[5].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[5].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[5].difference.value,
                        category.analysisYoYCategoryMonthData[5].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[6].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[6].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[6].difference.value,
                        category.analysisYoYCategoryMonthData[6].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[7].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[7].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[7].difference.value,
                        category.analysisYoYCategoryMonthData[7].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[8].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[8].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[8].difference.value,
                        category.analysisYoYCategoryMonthData[8].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[9].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[9].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[9].difference.value,
                        category.analysisYoYCategoryMonthData[9].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[10].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[10].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[10].difference.value,
                        category.analysisYoYCategoryMonthData[10].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryMonthData[11].lastYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[11].thisYearExpenseAmount.value,
                        category.analysisYoYCategoryMonthData[11].difference.value,
                        category.analysisYoYCategoryMonthData[11].yoyRatio.value
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryAverageData.lastYearExpenseAmount.value,
                        category.analysisYoYCategoryAverageData.thisYearExpenseAmount.value,
                        category.analysisYoYCategoryAverageData.difference.value,
                        category.analysisYoYCategoryAverageData.yoyRatio.value,
                    ),
                    AnalysisYoYResponse.MonthlyDataObject(
                        category.analysisYoYCategoryAverageData.lastYearExpenseAmount.value,
                        category.analysisYoYCategoryAverageData.thisYearExpenseAmount.value,
                        category.analysisYoYCategoryAverageData.difference.value,
                        category.analysisYoYCategoryAverageData.yoyRatio.value
                    )
                )
            },
            AnalysisYoYResponse.TotalObject(
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY?.analysisYoYTotal?.analysisYoYTotalMonthData!![0].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[0].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[0].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[0].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[1].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[1].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[1].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[1].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[2].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[2].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[2].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[2].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[3].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[3].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[3].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[3].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[4].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[4].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[4].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[4].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[5].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[5].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[5].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[5].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[6].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[6].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[6].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[6].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[7].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[7].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[7].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[7].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[8].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[8].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[8].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[8].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[9].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[9].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[9].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[9].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[10].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[10].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[10].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[10].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[11].lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[11].thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[11].difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalMonthData[11].yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalAverageData.lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalAverageData.thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalAverageData.difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalAverageData.yoyRatio.value,
                ),
                AnalysisYoYResponse.TotalMonthlyDataObject(
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalYearData.lastYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalYearData.thisYearExpenseAmount.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalYearData.difference.value,
                    serviceExecResult.analysisYoY.analysisYoYTotal.analysisYoYTotalYearData.yoyRatio.value,
                )
            )
        )

        val analysisYoYResponse =
            AnalysisYoYResponse(
                status,
                message,
                dataObject
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(analysisYoYResponse)
    }
}