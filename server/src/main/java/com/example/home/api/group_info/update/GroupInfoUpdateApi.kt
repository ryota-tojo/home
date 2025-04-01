package com.example.home.api.group_info.update


import com.example.home.api.ErrorResponse
import com.example.home.api.group_info.update.request.GroupInfoUpdateRequest
import com.example.home.api.group_info.update.response.GroupInfoUpdateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupApprovalFlg
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import com.example.home.service.group.GroupInfoControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupInfoUpdateApi(
    private val groupInfoControlService: GroupInfoControlService

) {
    companion object {
        const val API_PATH = "api/group/info/update"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupInfoUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestUserId = UserId(request.userId)
        val requestLeader = if (request.leader == null) null else UserLeaderFlg(request.leader)
        val requestApproval = if (request.approval == null) null else GroupApprovalFlg(request.approval)

        val serviceExecResult =
            groupInfoControlService.update(requestGroupsId, requestUserId, requestLeader, requestApproval)

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
                parameter = "groups_id"
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
        val dataObject = serviceExecResult.updateRows

        val groupInfoUpdateResponse =
            GroupInfoUpdateResponse(
                status,
                message,
                GroupInfoUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupInfoUpdateResponse)
    }
}