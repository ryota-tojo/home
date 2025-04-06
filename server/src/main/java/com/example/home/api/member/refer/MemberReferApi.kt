package com.example.home.api.member.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.member.refer.request.MemberReferRequest
import com.example.home.api.member.refer.response.MemberReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.service.member.MemberService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberReferApi(
    private val memberService: MemberService

) {
    companion object {
        const val API_PATH = "api/member/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: MemberReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestMemberId = if (request.memberId == 0) null else request.memberId.let { MemberId(it) }
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId.let { GroupsId(it) }
        val requestMemberNo = if (request.memberNo == 0) null else request.memberNo.let { MemberNo(it) }

        val serviceExecResult = memberService.refer(
            requestMemberId, requestGroupsId, requestMemberNo
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "メンバー参照失敗"
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
        val dataObject = serviceExecResult.member?.map { member ->
            MemberReferResponse.MemberObject(
                member.id.value,
                member.groupsId.value,
                member.memberNo.value,
                member.memberName.value,
                member.deleted.value
            )
        }

        val memberReferResponse =
            MemberReferResponse(
                status,
                message,
                MemberReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(memberReferResponse)
    }
}