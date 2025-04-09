package com.example.home.api.fixed.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.fixed.refer.request.FixedReferRequest
import com.example.home.api.fixed.refer.response.FixedReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.fixed.FixedService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class FixedReferApi(
    private val fixedService: FixedService

) {
    companion object {
        const val API_PATH = "api/fixed/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: FixedReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = YYYY(request.yyyy)

        val serviceExecResult = fixedService.refer(
            requestGroupsId, requestYYYY, null
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "確定データ参照失敗"
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
            FixedReferResponse.FixedResultObject(
                serviceExecResult.fixedRefer[0].fixed,
                serviceExecResult.fixedRefer[1].fixed,
                serviceExecResult.fixedRefer[2].fixed,
                serviceExecResult.fixedRefer[3].fixed,
                serviceExecResult.fixedRefer[4].fixed,
                serviceExecResult.fixedRefer[5].fixed,
                serviceExecResult.fixedRefer[6].fixed,
                serviceExecResult.fixedRefer[7].fixed,
                serviceExecResult.fixedRefer[8].fixed,
                serviceExecResult.fixedRefer[9].fixed,
                serviceExecResult.fixedRefer[10].fixed,
                serviceExecResult.fixedRefer[11].fixed
            )

        val fixedReferResponse =
            FixedReferResponse(
                status,
                message,
                FixedReferResponse.DataObject(requestYYYY.value.toString(), dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(fixedReferResponse)
    }
}