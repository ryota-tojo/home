package com.example.home.api.shopping.get_all_members


import com.example.home.api.shopping.get_all_categories.request.ShoppingGetAllMemberRequest
import com.example.home.api.shopping.get_all_categories.response.ShoppingGetAllMemberResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.shopping.ShoppingService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingGetAllMembersApi(
    private val shoppingService: ShoppingService

) {
    companion object {
        const val API_PATH = "api/shopping/all_member"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingGetAllMemberRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = if (request.yyyy == null) null else request.yyyy.let { YYYY(it) }
        val requestMM = if (request.mm == null) null else request.mm.let { MM(it) }

        val serviceExecResult = shoppingService.getAllMembers(
            requestGroupsId,
            requestYYYY,
            requestMM,
        )

        // 成功時のレスポンス
        val status = "success"
        val message = ResponseCode.成功.message
        val dataObject = serviceExecResult.map { member ->
            ShoppingGetAllMemberResponse.MemberObject(
                member.id.value,
                member.groupsId.value,
                member.memberNo.value,
                member.memberName.value,
            )
        }

        val shoppingGetAllMemberResponse =
            ShoppingGetAllMemberResponse(
                status,
                message,
                ShoppingGetAllMemberResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingGetAllMemberResponse)
    }
}