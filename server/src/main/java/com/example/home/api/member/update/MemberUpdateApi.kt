package com.example.home.api.member.update


import com.example.home.api.ErrorResponse
import com.example.home.api.member.update.request.MemberUpdateRequest
import com.example.home.api.member.update.response.MemberUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.service.member.MemberService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberUpdateApi(
    private val memberService: MemberService

) {
    companion object {
        const val API_PATH = "api/member/update"
    }

    @PostMapping(value = [API_PATH])
    fun update(
        @RequestBody @Valid request: MemberUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestMemberId = MemberId(request.memberId)
        val requestMemberNo = if (request.memberNo == 0) null else request.memberNo?.let { MemberNo(it) }
        val requestMemberName =
            if (request.memberName == "") null else request.memberName?.let { MemberName(it) }

        val serviceExecResult = memberService.update(
            requestMemberId, requestMemberNo, requestMemberName
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "メンバー更新失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.バリデーションエラー.message
            }
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

        val memberUpdateResponse =
            MemberUpdateResponse(
                status,
                message,
                MemberUpdateResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberUpdateResponse)
    }
}