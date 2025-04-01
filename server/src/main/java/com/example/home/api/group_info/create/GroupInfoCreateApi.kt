package com.example.home.api.group_info.create


import com.example.home.api.ErrorResponse
import com.example.home.api.group_info.create.request.GroupInfoCreateRequest
import com.example.home.api.group_info.create.response.GroupInfoCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import com.example.home.service.group.GroupInfoControlService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupInfoCreateApi(
    private val groupInfoControlService: GroupInfoControlService

) {
    companion object {
        const val API_PATH = "api/group/info/create"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupInfoCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestUserId = UserId(request.userId)
        val requestLeader = UserLeaderFlg(request.leader)

        val serviceExecResult = groupInfoControlService.save(requestGroupsId, requestUserId, requestLeader)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ情報登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.存在しないユーザー.code) {
                parameter = "user_id"
                errorMessage = ResponseCode.存在しないユーザー.message
            }
            if (serviceExecResult.result == ResponseCode.既にリーダーが存在するグループ.code) {
                parameter = "leader"
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
        val dataObject = GroupInfoCreateResponse.GroupInfoObject(
            serviceExecResult.groupInfo?.groupsId?.value,
            serviceExecResult.groupInfo?.userId?.value.toString(),
            serviceExecResult.groupInfo?.userLeaderFlg?.value.toString(),
            serviceExecResult.groupInfo?.groupApprovalFlg?.value.toString(),
            parseLocalDateTime(serviceExecResult.groupInfo?.createDate.toString()),
            parseLocalDateTime(serviceExecResult.groupInfo?.updateDate.toString()),
        )

        val groupInfoCreateResponse =
            GroupInfoCreateResponse(
                status,
                message,
                GroupInfoCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupInfoCreateResponse)
    }
}