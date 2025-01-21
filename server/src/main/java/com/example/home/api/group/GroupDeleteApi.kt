package com.example.home.api.group

import com.example.home.api.group.request.GroupDeleteRequest
import com.example.home.api.group.response.GroupDeleteResponse
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

        if (result.result == "VALIDATION_ERROR") {
            val groupDeleteResponseData: GroupDeleteResponse.GroupEntryResponseData =
                GroupDeleteResponse.GroupEntryResponseData(groupsId.value)
            val response = GroupDeleteResponse(
                "error",
                result.result,
                "使用できない文字列が含まれています",
                groupDeleteResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        if (result.result == "DATA_NOT_FOUND_ERROR") {
            val groupDeleteResponseData: GroupDeleteResponse.GroupEntryResponseData =
                GroupDeleteResponse.GroupEntryResponseData(groupsId.value)
            val response = GroupDeleteResponse(
                "error",
                result.result,
                "存在しない所属グループです",
                groupDeleteResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        val groupDeleteResponseData: GroupDeleteResponse.GroupEntryResponseData =
            GroupDeleteResponse.GroupEntryResponseData(groupsId.value,result.groupListDeletedResult,result.groupSettingDeletedResult)
        val response = GroupDeleteResponse(
            "success",
            result.result,
            "所属グループ(${groupsId.value})を削除しました",
            groupDeleteResponseData
        )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)

    }
}