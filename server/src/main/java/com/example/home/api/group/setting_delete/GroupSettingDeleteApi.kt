package com.example.home.api.group.setting_delete


import com.example.home.api.ErrorResponse
import com.example.home.api.group.create.request.GroupSettingDeleteRequest
import com.example.home.api.group.delete.request.GroupDeleteRequest
import com.example.home.api.group.delete.response.GroupDeleteResponse
import com.example.home.api.group.setting_delete.response.GroupSettingDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupSettingKey
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
class GroupSettingDeleteApi(
    private val groupControlService: GroupControlService

) {
    companion object {
        const val API_PATH = "api/group/delete/setting"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupSettingDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestSettingKey:GroupSettingKey? = request.settingKey?.let{GroupSettingKey(it)}


        val serviceExecResult = groupControlService.settingDelete(requestGroupsId,requestSettingKey)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ設定削除失敗"
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
        val dataObject = GroupSettingDeleteResponse.DataObject(
            serviceExecResult.deleteRows
        )

        val groupSettingDeleteResponse =
            GroupSettingDeleteResponse(
                status,
                message,
                dataObject
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupSettingDeleteResponse)
    }
}