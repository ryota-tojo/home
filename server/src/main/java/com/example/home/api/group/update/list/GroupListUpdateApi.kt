package com.example.home.api.group.update.list


import com.example.home.api.ErrorResponse
import com.example.home.api.group.update.list.request.GroupListUpdateRequest
import com.example.home.api.group.update.list.response.GroupListUpdateResponse
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
class GroupListUpdateApi(
    private val groupControlService: GroupControlService

) {
    companion object {
        const val API_PATH = "api/group/update/list"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupListUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestGroupName = if (request.groupName == "") null else request.groupName?.let { GroupName(it) }
        val requestGroupPassword =
            if (request.groupPassword == "") null else request.groupPassword?.let { GroupPassword(it) }

        val serviceExecResult = groupControlService.listUpdate(requestGroupsId, requestGroupName, requestGroupPassword)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ一覧更新失敗"
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
        val dataObject = serviceExecResult.updateRow

        val groupListUpdateResponse =
            GroupListUpdateResponse(
                status,
                message,
                GroupListUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupListUpdateResponse)
    }
}