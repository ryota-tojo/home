package com.example.home.api.group.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.group.refer.request.GroupReferRequest
import com.example.home.api.group.refer.response.GroupReferResponse
import com.example.home.api.group.refer.response.GroupReferResponse.GroupListObject
import com.example.home.api.group.refer.response.GroupReferResponse.GroupSettingObject
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
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
    private val groupControlService: GroupControlService

) {
    companion object {
        const val API_PATH = "api/group/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }

        val serviceExecResult = groupControlService.refer(requestGroupsId)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ参照失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "groups_id"
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
        val dataObject = serviceExecResult.groupListAndSetting?.map { group ->
            GroupReferResponse.GroupObject(
                // userInfo の各フィールドの null チェックとデフォルト値の設定
                GroupListObject(
                    group.groupList.id.toString(),
                    group.groupList.groupsId.value,
                    group.groupList.groupName.value,
                    group.groupList.groupPassword.value,
                ),

                group.groupSetting.map { groupSetting ->
                    GroupSettingObject(
                        groupSetting.settingKey.value,
                        groupSetting.settingValue.value,
                    )
                }
            )
        }

        val groupReferResponse =
            GroupReferResponse(
                status,
                message,
                GroupReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupReferResponse)
    }
}