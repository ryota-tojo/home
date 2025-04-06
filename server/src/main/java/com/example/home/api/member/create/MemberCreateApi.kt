package com.example.home.api.member.create


import com.example.home.api.ErrorResponse
import com.example.home.api.member.create.request.MemberCreateRequest
import com.example.home.api.member.create.response.MemberCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
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
class MemberCreateApi(
    private val memberService: MemberService

) {
    companion object {
        const val API_PATH = "api/member/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: MemberCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestMemberNo = MemberNo(request.memberNo)
        val requestMemberName = MemberName(request.memberName)

        val serviceExecResult = memberService.save(
            requestGroupsId, requestMemberNo, requestMemberName
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "メンバー登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.バリデーションエラー.message
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
        val dataObject = MemberCreateResponse.MemberObject(
            serviceExecResult.member?.id?.value,
            serviceExecResult.member?.groupsId?.value,
            serviceExecResult.member?.memberNo?.value,
            serviceExecResult.member?.memberName?.value,
        )

        val memberCreateResponse =
            MemberCreateResponse(
                status,
                message,
                MemberCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberCreateResponse)
    }
}