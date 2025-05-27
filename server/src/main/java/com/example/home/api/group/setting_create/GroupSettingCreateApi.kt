package com.example.home.api.group.setting_create


import com.example.home.api.ErrorResponse
import com.example.home.api.group.create.request.GroupSettingCreateRequest
import com.example.home.api.group.create.request.GroupSettingDeleteRequest
import com.example.home.api.group.create.response.GroupSettingCreateResponse

import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.*
import com.example.home.service.group.GroupControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupSettingCreateApi(
    private val groupControlService: GroupControlService

) {
    companion object {
        const val API_PATH = "api/group/create/setting"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupSettingCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestSettingKey = GroupSettingKey(request.settingKey)
        val requestSettingValue = GroupSettingValue(request.settingValue)

        val serviceExecResult = groupControlService.settingSave(requestGroupsId, requestSettingKey, requestSettingValue)

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

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "groups_id"
                errorMessage = ResponseCode.データ不在エラー.message
            }

            if (serviceExecResult.result == ResponseCode.重複エラー.code) {
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
        val dataObject = GroupSettingCreateResponse.GroupSettingObject(
            serviceExecResult.groupSetting?.settingKey?.value,
            serviceExecResult.groupSetting?.settingValue?.value
        )

        val groupCreateResponse =
            GroupSettingCreateResponse(
                status,
                message,
                GroupSettingCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupCreateResponse)
    }
}