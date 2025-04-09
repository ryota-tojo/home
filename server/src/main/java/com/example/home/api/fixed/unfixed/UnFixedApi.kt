package com.example.home.api.fixed.unfixed


import com.example.home.api.ErrorResponse
import com.example.home.api.fixed.fixed.request.FixedRequest
import com.example.home.api.fixed.unfixed.response.UnFixedResponse
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
class UnFixedApi(
    private val fixedService: FixedService

) {
    companion object {
        const val API_PATH = "api/fixed/unfixed"
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

        val serviceExecResult = fixedService.unFixed(
            requestGroupsId, requestYYYY, requestMM
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "データ確定解除失敗"
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
            UnFixedResponse.UnFixedRowsObject(
                serviceExecResult.budgetsFixedRows,
                serviceExecResult.shoppingFixedRows,
                serviceExecResult.commentFixedRows
            )

        val unfixedResponse =
            UnFixedResponse(
                status,
                message,
                UnFixedResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(unfixedResponse)
    }
}