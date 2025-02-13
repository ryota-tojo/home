package com.example.home.api.group

import com.example.home.api.group.request.GroupSettingUpdateRequest
import com.example.home.api.group.response.GroupSettingUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.group.GroupSettingRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.group.GroupControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupSettingUpdateApi(
    private val groupListRepository: GroupListRepository,
    private val groupSettingRepository: GroupSettingRepository,
    private val groupInfoRepository: GroupInfoRepository,
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val commentRepository: CommentRepository
) {
    companion object {
        const val API_PATH = "api/group/update/setting"
    }

    @PostMapping(value = [API_PATH])
    fun groupSettingUpdate(
        @RequestBody @Valid request: GroupSettingUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<GroupSettingUpdateResponse> {
        val groupsId = GroupsId(request.groupsId)
        val settingKey = GroupSettingKey(request.groupSettingKey)
        val settingValue = GroupSettingValue(request.groupSettingValue)

        val groupControlService: GroupControlService = GroupControlService(
            groupListRepository,
            groupSettingRepository,
            groupInfoRepository,
            memberRepository,
            categoryRepository,
            commentRepository
        )
        val result = groupControlService.settingUpdate(groupsId, settingKey, settingValue)

        val groupSettingUpdateResponseData: GroupSettingUpdateResponse.GroupSettingUpdateResponseData =
            GroupSettingUpdateResponse.GroupSettingUpdateResponseData(
                result.updateRows
            )

        if (result.result == ResponseCode.バリデーションエラー.code) {
            val response = GroupSettingUpdateResponse(
                ResponseCode.バリデーションエラー.status,
                ResponseCode.バリデーションエラー.code,
                ResponseCode.バリデーションエラー.message,
                groupSettingUpdateResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        if (result.result == ResponseCode.データ不在エラー.code) {
            val response = GroupSettingUpdateResponse(
                ResponseCode.データ不在エラー.status,
                ResponseCode.データ不在エラー.code,
                ResponseCode.データ不在エラー.message,
                groupSettingUpdateResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        val response = GroupSettingUpdateResponse(
            ResponseCode.成功.status,
            ResponseCode.成功.code,
            ResponseCode.成功.message,
            groupSettingUpdateResponseData
        )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)

    }
}