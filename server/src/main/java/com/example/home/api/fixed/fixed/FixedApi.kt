package com.example.home.api.fixed.fixed


import com.example.home.api.ErrorResponse
import com.example.home.api.fixed.fixed.request.FixedRequest
import com.example.home.api.fixed.fixed.response.FixedResponse
import com.example.home.api.fixed.fixed.response.FixedResponse.FixedRowsObject
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.fixed.FixedService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FixedApi(
    private val fixedService: FixedService

) {
    companion object {
        const val API_PATH = "api/fixed/fixed"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: FixedRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = YYYY(request.yyyy)
        val requestMM = MM(request.mm)

        val serviceExecResult = fixedService.fixed(
            requestGroupsId, requestYYYY, requestMM
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "データ確定失敗"
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
        val dataObject =
            FixedRowsObject(
                serviceExecResult.budgetsFixedRows,
                serviceExecResult.shoppingFixedRows,
                serviceExecResult.commentFixedRows
            )

        val fixedResponse =
            FixedResponse(
                status,
                message,
                FixedResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fixedResponse)
    }
}