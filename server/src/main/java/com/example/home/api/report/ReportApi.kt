package com.example.home.api.report


import com.example.home.api.ErrorResponse
import com.example.home.api.report.request.ReportRequest
import com.example.home.api.report.response.ReportResponse
import com.example.home.api.report.response.ReportResponse.UnSettlementDataObject
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.report.ReportService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportApi(
    private val reportService: ReportService

) {
    companion object {
        const val API_PATH = "api/report"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ReportRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestGroupPassword = GroupPassword(request.groupPassword)
        val requestYYYY = YYYY(request.yyyy)
        val requestMM = MM(request.mm)

        val serviceExecResult = reportService.createReport(
            requestGroupsId, requestGroupPassword, requestYYYY, requestMM
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "確定データ参照失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.グループ認証エラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.グループ認証エラー.message
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
        val dataObject = ReportResponse.ReportObject(
            requestYYYY.value,
            requestMM.value,
            ReportResponse.SettlementReportObject(
                serviceExecResult.report?.settlementReport?.dataCount,
                serviceExecResult.report?.settlementReport?.memberAndAmountList?.map { memberAndAmount ->
                    UnSettlementDataObject(
                        memberAndAmount.member.id.value,
                        memberAndAmount.member.memberName.value,
                        memberAndAmount.amount.value
                    )
                },
                serviceExecResult.report?.settlementReport?.totalAmount?.value
            ),
            ReportResponse.BalanceReportObject(
                serviceExecResult.report?.balanceReport?.categoryAndAmountList?.map { categoryAndAmount ->
                    ReportResponse.CategoryListObject(
                        categoryAndAmount.category.id.value,
                        categoryAndAmount.category.categoryName.value,
                        categoryAndAmount.amount.value
                    )
                },
                serviceExecResult.report?.balanceReport?.totalAmount?.value
            )
        )

        val reportResponse =
            ReportResponse(
                status,
                message,
                ReportResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(reportResponse)
    }
}