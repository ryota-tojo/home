package com.example.home.api.group_info.leaderChange


import com.example.home.api.ErrorResponse
import com.example.home.api.analysis.yoy.response.AnalysisYoYResponse
import com.example.home.api.group_info.leaderChange.request.GroupInfoLeaderChangeRequest
import com.example.home.api.group_info.leaderChange.response.GroupInfoLeaderChangeResponse
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
class GroupInfoLeaderChangeApi(
    private val groupInfoControlService: GroupInfoControlService

) {
    companion object {
        const val API_PATH = "api/group/info/leader_change"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupInfoLeaderChangeRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestLeaderUserId = UserId(request.leaderUserId)
        val requestNewLeaderUserId = UserId(request.newLeaderUserId)

        val serviceExecResult =
            groupInfoControlService.leaderChange(requestGroupsId, requestLeaderUserId, requestNewLeaderUserId,)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "リーダー更新失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.存在しないユーザー.code) {
                parameter = ""
                errorMessage = ResponseCode.存在しないユーザー.message
            }
            if (serviceExecResult.result == ResponseCode.リーダー以外のユーザー.code) {
                parameter = ""
                errorMessage = ResponseCode.リーダー以外のユーザー.message
            }
            if (serviceExecResult.result == ResponseCode.メンバー以外のユーザー.code) {
                parameter = ""
                errorMessage = ResponseCode.メンバー以外のユーザー.message
            }
            if (serviceExecResult.result == ResponseCode.未承認のユーザー.code) {
                parameter = ""
                errorMessage = ResponseCode.未承認のユーザー.message
            }
            if (serviceExecResult.result == ResponseCode.グループ情報の更新に失敗.code) {
                parameter = ""
                errorMessage = ResponseCode.グループ情報の更新に失敗.message
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
        val dataObject = GroupInfoLeaderChangeResponse.DataObject(
            serviceExecResult.leader.value,
            serviceExecResult.newLeader.value,
        )

            val groupInfoLeaderChangeResponse =
                GroupInfoLeaderChangeResponse(
                    status,
                    message,
                    dataObject
                )
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(groupInfoLeaderChangeResponse)
    }
}