package com.example.home.api.master.setting.create


import com.example.home.api.ErrorResponse
import com.example.home.api.master.setting.create.request.MasterSettingCreateRequest
import com.example.home.api.master.setting.create.response.MasterSettingCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.domain.value_object.master.MasterSettingRemarks
import com.example.home.domain.value_object.master.MasterSettingValue
import com.example.home.service.master.setting.MasterSettingService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MasterSettingCreateApi(
    private val masterSettingService: MasterSettingService

) {
    companion object {
        const val API_PATH = "api/master/setting/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: MasterSettingCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestKey = MasterSettingKey(request.settingKey)
        val requestValue = MasterSettingValue(request.settingValue)
        val requestRemarks = MasterSettingRemarks(request.remarks)

        val serviceExecResult = masterSettingService.save(
            requestKey, requestValue, requestRemarks
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "マスタ設定登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.バリデーションエラー.message
            }
            if (serviceExecResult.result == ResponseCode.重複エラー.code) {
                parameter = "setting_key"
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
        val dataObject = MasterSettingCreateResponse.SettingObject(
            serviceExecResult.masterSetting?.masterSettingKey?.value,
            serviceExecResult.masterSetting?.masterSettingValue?.value,
            serviceExecResult.masterSetting?.masterSettingRemarks?.value
        )

        val masterSettingCreateResponse =
            MasterSettingCreateResponse(
                status,
                message,
                MasterSettingCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(masterSettingCreateResponse)
    }
}