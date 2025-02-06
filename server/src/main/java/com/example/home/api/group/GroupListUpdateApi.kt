package com.example.home.api.group

import com.example.home.api.group.request.GroupListUpdateRequest
import com.example.home.api.group.response.GroupListUpdateResponse
import com.example.home.api.group.response.GroupListUpdateResponse.GroupListUpdateResponseData
import com.example.home.domain.group.GroupList
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.repository.group.GroupListRepository
import com.example.home.infrastructure.persistence.repository.group.GroupSettingRepository
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

        val groupControlService: GroupControlService = GroupControlService(groupListRepository, groupSettingRepository)
        val result = groupControlService.listUpdate(groupsId,groupName)

        if (result.result == "VALIDATION_ERROR") {
            val groupListUpdateResponseData: GroupListUpdateResponseData =
                GroupListUpdateResponse.GroupListUpdateResponseData(
                    result.updateRowSet
                )
            val response = GroupListUpdateResponse(
                "error",
                result.result,
                "使用できない文字列が含まれています",
                groupListUpdateResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        if (result.result == "DATA_NOT_FOUND_ERROR") {
            val groupListUpdateResponseData: GroupListUpdateResponseData =
                GroupListUpdateResponse.GroupListUpdateResponseData(
                    result.updateRowSet
                )
            val response = GroupListUpdateResponse(
                "error",
                result.result,
                "存在しない所属グループです",
                groupListUpdateResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        val groupListUpdateResponseData: GroupListUpdateResponseData =
            GroupListUpdateResponse.GroupListUpdateResponseData(
                result.updateRowSet
            )
        val response = GroupListUpdateResponse(
            "success",
            result.result,
            "所属グループ一覧(${groupsId.value})を更新しました",
            groupListUpdateResponseData
        )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)

    }
}