package com.example.home.api.group.update.setting


import com.example.home.api.ErrorResponse
import com.example.home.api.group.update.setting.request.GroupSettingUpdateRequest
import com.example.home.api.group.update.setting.response.GroupSettingUpdateResponse
import com.example.home.domain.model.ResponseCode
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
    private val groupControlService: GroupControlService

) {
    companion object {
        const val API_PATH = "api/group/update/setting"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupSettingUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestGroupSettingKey = GroupSettingKey(request.groupSettingKey)
        val requestGroupSettingValue = GroupSettingValue(request.groupSettingValue)

        val serviceExecResult =
            groupControlService.settingUpdate(requestGroupsId, requestGroupSettingKey, requestGroupSettingValue)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ設定更新失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "groups_id, setting_key, setting_value"
                errorMessage = ResponseCode.バリデーションエラー.message
            }

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "groups_id, setting_key"
                errorMessage = ResponseCode.データ不在エラー.message
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
        val dataObject = serviceExecResult.updateRows

        val groupSettingUpdateResponse =
            GroupSettingUpdateResponse(
                status,
                message,
                GroupSettingUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupSettingUpdateResponse)
    }
}