package com.example.home.api.group

import com.example.home.api.group.request.GroupListUpdateRequest
import com.example.home.api.group.response.GroupListUpdateResponse
import com.example.home.api.group.response.GroupListUpdateResponse.GroupListUpdateResponseData
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.group.GroupSettingRepository
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupPassword
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
class GroupListUpdateApi(
    private val groupListRepository: GroupListRepository,
    private val groupSettingRepository: GroupSettingRepository
) {
    companion object {
        const val API_PATH = "api/group/update/list";
    }

    @PostMapping(value = [API_PATH])
    fun groupListUpdate(
        @RequestBody @Valid request: GroupListUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<GroupListUpdateResponse> {
        val groupsId = GroupsId(request.groupsId)
        val groupName = GroupName(request.groupName)
        val groupPassword = GroupPassword(request.groupPassword)

        val groupControlService: GroupControlService = GroupControlService(groupListRepository, groupSettingRepository)
        val result = groupControlService.listUpdate(groupsId, groupName, groupPassword)

        val groupListUpdateResponseData: GroupListUpdateResponseData =
            GroupListUpdateResponse.GroupListUpdateResponseData(
                result.updateRow
            )

        if (result.result == ResponseCode.バリデーションエラー.code) {
            val response = GroupListUpdateResponse(
                ResponseCode.バリデーションエラー.status,
                ResponseCode.バリデーションエラー.code,
                ResponseCode.バリデーションエラー.message,
                groupListUpdateResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        if (result.result == ResponseCode.データ不在エラー.code) {

            val response = GroupListUpdateResponse(
                ResponseCode.データ不在エラー.status,
                ResponseCode.データ不在エラー.code,
                ResponseCode.データ不在エラー.message,
                groupListUpdateResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        val response = GroupListUpdateResponse(
            ResponseCode.成功.status,
            ResponseCode.成功.code,
            ResponseCode.成功.message,
            groupListUpdateResponseData
        )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)

    }
}