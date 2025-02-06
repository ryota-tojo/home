package com.example.home.api.group

import com.example.home.api.group.request.GroupDeleteRequest
import com.example.home.api.group.response.GroupDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.group.GroupSettingRepository
import com.example.home.service.group.GroupControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupDeleteApi(
    private val groupListRepository: GroupListRepository,
    private val groupSettingRepository: GroupSettingRepository

) {
    companion object {
        const val API_PATH = "api/group/delete";
    }

    @PostMapping(value = [API_PATH])
    fun groupDelete(
        @RequestBody @Valid request: GroupDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<GroupDeleteResponse> {
        val groupsId = GroupsId(request.groupsId)

        val groupControlService: GroupControlService = GroupControlService(groupListRepository, groupSettingRepository)
        val result = groupControlService.delete(groupsId)

        if (result.result == ResponseCode.バリデーションエラー.code) {
            val groupDeleteResponseData: GroupDeleteResponse.GroupEntryResponseData =
                GroupDeleteResponse.GroupEntryResponseData(groupsId.value)
            val response = GroupDeleteResponse(
                ResponseCode.バリデーションエラー.status,
                ResponseCode.バリデーションエラー.code,
                ResponseCode.バリデーションエラー.message,
                groupDeleteResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        if (result.result == ResponseCode.データ不在エラー.code) {
            val groupDeleteResponseData: GroupDeleteResponse.GroupEntryResponseData =
                GroupDeleteResponse.GroupEntryResponseData(groupsId.value)
            val response = GroupDeleteResponse(
                ResponseCode.データ不在エラー.status,
                ResponseCode.データ不在エラー.code,
                ResponseCode.データ不在エラー.message,
                groupDeleteResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        val groupDeleteResponseData: GroupDeleteResponse.GroupEntryResponseData =
            GroupDeleteResponse.GroupEntryResponseData(
                groupsId.value,
                result.groupListDeletedResult,
                result.groupSettingDeletedResult
            )
        val response = GroupDeleteResponse(
            ResponseCode.成功.status,
            ResponseCode.成功.code,
            ResponseCode.成功.message,
            groupDeleteResponseData
        )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)

    }
}