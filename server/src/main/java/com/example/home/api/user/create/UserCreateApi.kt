package com.example.home.api.user.create


import com.example.home.api.ErrorResponse
import com.example.home.api.user.create.request.UserCreateRequest
import com.example.home.api.user.create.response.UserCreateResponse
import com.example.home.api.user.create.response.UserCreateResponse.UserInfoObject
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.user.*
import com.example.home.service.user.UserControlService
import com.example.home.util.ParseLocalDateTime.parseLocalDateTime
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserCreateApi(
    private val userControlService: UserControlService

) {
    companion object {
        const val API_PATH = "api/user/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: UserCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestUserName = UserName(request.userName)
        val requestPassword = UserPassword(request.password)
        val requestPermission = UserPermission(request.permission)
        val requestApproval = UserApprovalFlg(request.approval)
        val requestDelete = UserDeleteFlg(request.delete)

        val serviceExecResult = userControlService.save(
            requestUserName, requestPassword, requestPermission, requestApproval, requestDelete
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "ユーザー登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "user_name, password"
                errorMessage = ResponseCode.バリデーションエラー.message
            }

            if (serviceExecResult.result == ResponseCode.重複エラー.code) {
                parameter = "user_name"
                errorMessage = ResponseCode.重複エラー.message
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
        val dataObject = UserCreateResponse.UserObject(
            UserInfoObject(
                serviceExecResult.userRefer?.userInfo?.userId?.value.toString(),
                serviceExecResult.userRefer?.userInfo?.userName?.value.toString(),
                serviceExecResult.userRefer?.userInfo?.password?.value.toString(),
                serviceExecResult.userRefer?.userInfo?.permission?.value.toString(),
                serviceExecResult.userRefer?.userInfo?.approvalFlg?.value.toString(),
                serviceExecResult.userRefer?.userInfo?.deleteFlg?.value.toString(),
                parseLocalDateTime(serviceExecResult.userRefer?.userInfo?.createDate.toString()),
                parseLocalDateTime(serviceExecResult.userRefer?.userInfo?.updateDate.toString()),
                parseLocalDateTime(serviceExecResult.userRefer?.userInfo?.approvalDate.toString()),
                parseLocalDateTime(serviceExecResult.userRefer?.userInfo?.deleteDate.toString()),
            ),
            serviceExecResult.userRefer?.userSetting?.map { userSetting ->
                UserCreateResponse.UserSettingObject(
                    userSetting.userSettingKey.value,
                    userSetting.userSettingvalue.value
                )
            },
            serviceExecResult.userRefer?.groupInfo?.map { groupInfo ->
                UserCreateResponse.GroupInfoObject(
                    groupInfo.groupsId.value,
                    groupInfo.userLeaderFlg.value.toString(),
                    groupInfo.groupApprovalFlg.value.toString(),
                    parseLocalDateTime(groupInfo.createDate.toString()),
                    parseLocalDateTime(groupInfo.updateDate.toString())
                )
            }
        )

        val userCreateResponse =
            UserCreateResponse(
                status,
                message,
                UserCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(userCreateResponse)
    }
}