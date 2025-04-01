package com.example.home.api.group_info.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.group_info.delete.request.GroupInfoDeleteRequest
import com.example.home.api.group_info.delete.response.GroupInfoDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.group.GroupInfoControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupInfoDeleteApi(
    private val groupInfoControlService: GroupInfoControlService

) {
    companion object {
        const val API_PATH = "api/group/info/delete"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupInfoDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestUserId = if (request.userId == null) null else UserId(request.userId)

        val serviceExecResult = groupInfoControlService.delete(requestGroupsId, requestUserId)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ情報更新失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "groups_id, user_id"
                errorMessage = ResponseCode.データ不在エラー.message
            }
            if (serviceExecResult.result == ResponseCode.既にリーダーが存在するグループ.code) {
                parameter = "groups_id, user_id"
                errorMessage = ResponseCode.既にリーダーが存在するグループ.message
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
        val dataObject = serviceExecResult.deleteRows

        val groupInfoDeleteResponse =
            GroupInfoDeleteResponse(
                status,
                message,
                GroupInfoDeleteResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupInfoDeleteResponse)
    }
}