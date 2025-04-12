package com.example.home.api.group_info.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.group_info.refer.request.GroupInfoReferRequest
import com.example.home.api.group_info.refer.response.GroupInfoReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.user.UserId
import com.example.home.service.group.GroupInfoControlService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupInfoReferApi(
    private val groupInfoControlService: GroupInfoControlService

) {
    companion object {
        const val API_PATH = "api/group/info/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: GroupInfoReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestUserId = if (request.userId == null) null else UserId(request.userId)

        val requestOffset = request.offSet
        val requestLimit = request.limit

        val serviceExecResult =
            groupInfoControlService.refer(requestGroupsId, requestUserId, requestOffset, requestLimit)

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
        val dataObject = serviceExecResult.groupInfoList?.map { groupInfo ->
            GroupInfoReferResponse.GroupInfoObject(
                groupInfo.groupsId.value,
                groupInfo.userId.value.toString(),
                groupInfo.userLeaderFlg.value.toString(),
                groupInfo.groupApprovalFlg.value.toString(),
                parseLocalDateTime(groupInfo.createDate.toString()),
                parseLocalDateTime(groupInfo.updateDate.toString()),
            )
        }

        val groupInfoReferResponse =
            GroupInfoReferResponse(
                status,
                message,
                GroupInfoReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(groupInfoReferResponse)
    }
}