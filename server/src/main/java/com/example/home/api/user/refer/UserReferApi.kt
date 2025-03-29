package com.example.home.api.user.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.user.refer.request.UserReferRequest
import com.example.home.api.user.refer.response.UserReferResponse
import com.example.home.api.user.refer.response.UserReferResponse.UserInfoObject
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.UserId
import com.example.home.domain.value_object.user.UserName
import com.example.home.service.user.UserControlService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserReferApi(
    private val userControlService: UserControlService

) {
    companion object {
        const val API_PATH = "api/user/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: UserReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUserId = if (request.userId == 0) null else request.userId?.let { UserId(it) }
        val requestUserName = if (request.userName == "") null else request.userName?.let { UserName(it) }

        val serviceExecResult = userControlService.refer(requestUserId, requestUserName)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "ユーザー参照失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "user_id, user_name"
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
        val dataObject = serviceExecResult.userRefer?.map { user ->
            UserReferResponse.UserObject(
                // userInfo の各フィールドの null チェックとデフォルト値の設定
                UserInfoObject(
                    user.userInfo?.userId?.value.toString(),  // userId が null の場合は "未設定"
                    user.userInfo?.userName?.value ?: "",  // userName が null の場合は "未設定"
                    user.userInfo?.password?.value ?: "",  // password が null の場合は "未設定"
                    user.userInfo?.permission?.value?.toString() ?: "",  // permission が null の場合は ""
                    user.userInfo?.approvalFlg?.value?.toString() ?: "",  // approvalFlg が null の場合は ""
                    user.userInfo?.deleteFlg?.value?.toString() ?: "",  // deleteFlg が null の場合は ""
                    parseLocalDateTime(user.userInfo?.createDate?.toString() ?: ""),  // createDate が null の場合は ""
                    parseLocalDateTime(user.userInfo?.updateDate?.toString() ?: ""),  // updateDate が null の場合は ""
                    parseLocalDateTime(user.userInfo?.approvalDate?.toString() ?: ""),  // approvalDate が null の場合は ""
                    parseLocalDateTime(user.userInfo?.deleteDate?.toString() ?: "")  // deleteDate が null の場合は "未設定"
                ),
                user.userSetting?.map { userSetting ->
                    UserReferResponse.UserSettingObject(
                        userSetting.userSettingKey.value,
                        userSetting.userSettingvalue.value
                    )
                },
                user.groupInfo?.map { groupInfo ->
                    UserReferResponse.GroupInfoObject(
                        groupInfo.groupsId.value,
                        groupInfo.userLeaderFlg.value.toString(),
                        groupInfo.groupApprovalFlg.value.toString(),
                        parseLocalDateTime(groupInfo.createDate.toString()),
                        parseLocalDateTime(groupInfo.updateDate.toString())
                    )
                }
            )
        }

        val userReferResponse =
            UserReferResponse(
                status,
                message,
                UserReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userReferResponse)
    }
}