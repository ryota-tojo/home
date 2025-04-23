package com.example.home.api.master.setting.update


import com.example.home.api.ErrorResponse
import com.example.home.api.master.setting.update.request.MasterSettingUpdateRequest
import com.example.home.api.master.setting.update.response.MasterSettingUpdateResponse
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
class MasterSettingUpdateApi(
    private val masterSettingService: MasterSettingService

) {
    companion object {
        const val API_PATH = "api/master/setting/update"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: MasterSettingUpdateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestKey = MasterSettingKey(request.settingKey)
        val requestValue: MasterSettingValue? = request.settingValue?.let { MasterSettingValue(it) }
        val requestRemarks: MasterSettingRemarks? = request.remarks?.let { MasterSettingRemarks(it) }

        val serviceExecResult = masterSettingService.update(
            requestKey, requestValue, requestRemarks
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "マスタ設定更新失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.バリデーションエラー.message
            }
            if (serviceExecResult.result == ResponseCode.データ不在エラー.code) {
                parameter = "setting_key"
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
        val dataObject = serviceExecResult.updateRows

        val masterSettingUpdateResponse =
            MasterSettingUpdateResponse(
                status,
                message,
                MasterSettingUpdateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(masterSettingUpdateResponse)
    }
}