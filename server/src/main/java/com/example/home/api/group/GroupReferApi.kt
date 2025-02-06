package com.example.home.api.group

import com.example.home.api.group.request.GroupReferRequest
import com.example.home.api.group.response.GroupListResponse
import com.example.home.api.group.response.GroupReferResponse
import com.example.home.api.group.response.GroupSettingResponse
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.repository.group.GroupListRepository
import com.example.home.infrastructure.persistence.repository.group.GroupSettingRepository
import com.example.home.service.group.GroupControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupReferApi(
    private val groupListRepository: GroupListRepository,
    private val groupSettingRepository: GroupSettingRepository

) {
    companion object {
        const val API_PATH = "api/group/refer";
    }

    @GetMapping(value = [API_PATH])
    fun groupRefer(
        @RequestBody @Valid request: GroupReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<GroupReferResponse> {
        val groupsId = GroupsId(request.groupsId ?: "")

        val groupControlService: GroupControlService = GroupControlService(groupListRepository, groupSettingRepository)
        val result = if (groupsId.toString() != "") {
            groupControlService.refer(groupsId)
        } else {
            groupControlService.referAll()
        }

        val groupListResponse = result.groupListAndSetting.groupList.map{ groupList ->
            GroupListResponse(
                groupId = groupList.id,
                groupsId = groupList.groupsId.value,
                groupName = groupList.groupName.value
            )
        }
        val groupSettingResponse = result.groupListAndSetting.groupSetting.map{ groupSetting ->
            GroupSettingResponse(
                id = groupSetting.id,
                groupsId = groupSetting.groupsId.value,
                groupSettingKey = groupSetting.settingKey.value,
                groupSettingValue = groupSetting.settingValue.value
            )
        }

        val groupReferResponseData: GroupReferResponse.GroupReferResponseData =
            GroupReferResponse.GroupReferResponseData(
                groupListResponse,
                groupSettingResponse
            )

        val response = GroupReferResponse(
            "success",
            "SUCCESS",
            "所属グループ情報を取得しました",
            groupReferResponseData
        )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)
    }
}