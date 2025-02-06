package com.example.home.api.group

import com.example.home.api.group.request.GroupSettingUpdateRequest
import com.example.home.api.group.response.GroupSettingUpdateResponse
import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue
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
class GroupSettingUpdateApi(
    private val groupListRepository: GroupListRepository,
    private val groupSettingRepository: GroupSettingRepository
) {
    companion object {
        const val API_PATH = "api/group/update/setting";
    }

    @PostMapping(value = [API_PATH])
    fun groupSettingUpdate(
        @RequestBody @Valid request: GroupSettingUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<GroupSettingUpdateResponse> {
        val groupsId = GroupsId(request.groupsId)
        val settingKey = GroupSettingKey(request.groupSettingKey)
        val settingValue = GroupSettingValue(request.groupSettingValue)

        val groupControlService: GroupControlService = GroupControlService(groupListRepository, groupSettingRepository)
        val result = groupControlService.SettingUpdate(groupsId, settingKey, settingValue)

        if (result.result == "VALIDATION_ERROR") {
            val groupSettingUpdateResponseData: GroupSettingUpdateResponse.GroupSettingUpdateResponseData =
                GroupSettingUpdateResponse.GroupSettingUpdateResponseData(
                    result.updateRows
                )
            val response = GroupSettingUpdateResponse(
                "error",
                result.result,
                "使用できない文字列が含まれていますvalue",
                groupSettingUpdateResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        if (result.result == "DATA_NOT_FOUND_ERROR") {
            val groupSettingUpdateResponseData: GroupSettingUpdateResponse.GroupSettingUpdateResponseData =
                GroupSettingUpdateResponse.GroupSettingUpdateResponseData(
                    result.updateRows
                )
            val response = GroupSettingUpdateResponse(
                "error",
                result.result,
                "存在しない所属グループです",
                groupSettingUpdateResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        val groupSettingUpdateResponseData: GroupSettingUpdateResponse.GroupSettingUpdateResponseData =
            GroupSettingUpdateResponse.GroupSettingUpdateResponseData(
                result.updateRows
            )
        val response = GroupSettingUpdateResponse(
            "success",
            result.result,
            "所属グループ設定(${groupsId.value})を更新しました",
            groupSettingUpdateResponseData
        )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)

    }
}