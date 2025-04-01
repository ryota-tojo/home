package com.example.home.api.group.create


import com.example.home.api.ErrorResponse
import com.example.home.api.group.create.request.GroupCreateRequest
import com.example.home.api.group.create.response.GroupCreateResponse

import com.example.home.domain.model.ResponseCode
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
class GroupCreateApi(
    private val groupControlService: GroupControlService

) {
    companion object {
        const val API_PATH = "api/group/create"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestGroupName = GroupName(request.groupName)
        val requestGroupPassword = GroupPassword(request.groupPassword)

        val serviceExecResult = groupControlService.save(requestGroupsId, requestGroupName, requestGroupPassword)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "groups_id, group_name, group_password"
                errorMessage = ResponseCode.バリデーションエラー.message
            }

            if (serviceExecResult.result == ResponseCode.重複エラー.code) {
                parameter = "groups_id"
                errorMessage = ResponseCode.重複エラー.message
            }

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
        val dataObject = GroupCreateResponse.GroupObject(

            GroupCreateResponse.GroupListObject(
                serviceExecResult.groupList?.id.toString(),
                serviceExecResult.groupList?.groupsId?.value,
                serviceExecResult.groupList?.groupName?.value,
                serviceExecResult.groupList?.groupPassword?.value
            ),
            serviceExecResult.groupSettingList?.map { groupSetting ->
                GroupCreateResponse.GroupSettingObject(
                    groupSetting.settingKey.value,
                    groupSetting.settingValue.value,
                )
            }
        )

        val groupCreateResponse =
            GroupCreateResponse(
                status,
                message,
                GroupCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupCreateResponse)
    }
}