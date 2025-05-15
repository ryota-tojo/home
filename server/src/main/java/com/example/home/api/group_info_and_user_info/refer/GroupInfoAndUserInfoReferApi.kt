package com.example.home.api.group_info_and_user_info.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.group_info_and_user_info.refer.request.GroupInfoAndUserInfoReferRequest
import com.example.home.api.group_info_and_user_info.refer.response.GroupInfoAndUserInfoReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserLeaderFlg
import com.example.home.service.group.GroupInfoAndUserInfoControlService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupInfoAndUserInfoAndUserInfoReferApi(
    private val groupInfoAndUserInfoControlService: GroupInfoAndUserInfoControlService

) {
    companion object {
        const val API_PATH = "api/group/info/user_refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupInfoAndUserInfoReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestUserId = if (request.userId == null) null else UserId(request.userId)
        val requestLeader = if (request.leader == null) null else UserLeaderFlg(request.leader)

        val requestOffset = request.offSet
        val requestLimit = request.limit

        val serviceExecResult =
            groupInfoAndUserInfoControlService.refer(requestGroupsId, requestUserId, requestLeader, requestOffset, requestLimit)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "所属グループ情報参照失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "-"
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
        val dataObject = serviceExecResult.groupInfoAndUserInfoList?.map { groupInfo ->
            GroupInfoAndUserInfoReferResponse.GroupInfoObject(
                groupInfo.groupsId.value,
                groupInfo.userId.value.toString(),
                groupInfo.userName.value.toString(),
                groupInfo.userPermission.value.toString(),
                groupInfo.userApprovalFlg.value.toString(),
                groupInfo.userDeleteFlg.value.toString(),
                groupInfo.userLeaderFlg.value.toString(),
                groupInfo.groupApprovalFlg.value.toString(),
            )
        }

        val groupInfoReferResponse =
            GroupInfoAndUserInfoReferResponse(
                status,
                message,
                GroupInfoAndUserInfoReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupInfoReferResponse)
    }
}