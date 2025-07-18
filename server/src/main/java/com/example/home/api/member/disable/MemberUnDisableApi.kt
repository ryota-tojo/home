package com.example.home.api.member.disable


import com.example.home.api.ErrorResponse
import com.example.home.api.member.disable.request.MemberDisableRequest
import com.example.home.api.member.disable.response.MemberDisableResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.member.MemberId
import com.example.home.service.member.MemberService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberUnDisableApi(
    private val memberService: MemberService

) {
    companion object {
        const val API_PATH = "api/member/un-disable"
    }

    @PostMapping(value = [API_PATH])
    fun disable(
        @RequestBody @Valid request: MemberDisableRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestMemberId = MemberId(request.memberId)

        val serviceExecResult = memberService.setUnDeleted(requestMemberId)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "メンバー有効化失敗"
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

        val memberDisableResponse =
            MemberDisableResponse(
                status,
                message,
                MemberDisableResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberDisableResponse)
    }
}