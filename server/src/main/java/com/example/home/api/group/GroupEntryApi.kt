package com.example.home.api.group

import com.example.home.api.group.request.GroupEntryRequest
import com.example.home.api.group.response.GroupEntryResponse
import com.example.home.api.group.response.GroupListResponse
import com.example.home.api.group.response.GroupSettingResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.group.GroupSettingRepository
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.service.group.GroupControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupEntryApi(
    private val groupListRepository: GroupListRepository,
    private val groupSettingRepository: GroupSettingRepository

) {
    companion object {
        const val API_PATH = "api/group/entry";
    }

    @PostMapping(value = [API_PATH])
    fun groupEntry(
        @RequestBody @Valid request: GroupEntryRequest,
        response: HttpServletResponse
    ): ResponseEntity<GroupEntryResponse> {
        val groupsId = GroupsId(request.groupsId)
        val groupName = GroupName(request.groupName)
        val groupPassword = GroupPassword(request.groupPassword)

        val groupControlService: GroupControlService = GroupControlService(groupListRepository, groupSettingRepository)
        val result = groupControlService.save(groupsId, groupName, groupPassword)

        val groupListResponse =
            GroupListResponse(
                result.groupList?.id,
                result.groupList?.groupsId?.value,
                result.groupList?.groupName?.value
            )
        val groupSettingResponse = result.groupSettingList?.map { groupSetting ->
            GroupSettingResponse(
                id = groupSetting.id,
                groupsId = groupSetting.groupsId.value,
                groupSettingKey = groupSetting.settingKey.value,
                groupSettingValue = groupSetting.settingValue.value
            )
        }
        val groupEntryResponseData: GroupEntryResponse.GroupEntryResponseData =
            GroupEntryResponse.GroupEntryResponseData(groupListResponse, groupSettingResponse)

        if (result.result == ResponseCode.バリデーションエラー.code) {
            val response = GroupEntryResponse(
                ResponseCode.バリデーションエラー.status,
                ResponseCode.バリデーションエラー.code,
                ResponseCode.バリデーションエラー.message,
                groupEntryResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        if (result.result == ResponseCode.重複エラー.code) {
            val response = GroupEntryResponse(
                ResponseCode.重複エラー.status,
                ResponseCode.重複エラー.code,
                ResponseCode.重複エラー.message,
                groupEntryResponseData
            )
            return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response)
        }
        val response = GroupEntryResponse(
            ResponseCode.成功.status,
            ResponseCode.成功.code,
            ResponseCode.成功.message,
            groupEntryResponseData
        )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(response)

    }
}