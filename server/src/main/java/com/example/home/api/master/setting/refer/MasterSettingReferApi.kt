package com.example.home.api.master.setting.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.master.setting.refer.request.MasterSettingReferRequest
import com.example.home.api.master.setting.refer.response.MasterSettingReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.service.master.MasterSettingService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MasterSettingReferApi(
    private val masterSettingService: MasterSettingService

) {
    companion object {
        const val API_PATH = "api/master/setting/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: MasterSettingReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestSettingKey = if (request.settingKey == null) null else MasterSettingKey(request.settingKey)

        val serviceExecResult = masterSettingService.refer(
            requestSettingKey
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "マスター設定参照失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

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
        val dataObject = serviceExecResult.masterSettingList?.map { setting ->
            MasterSettingReferResponse.SettingObject(
                setting.masterSettingKey.value,
                setting.masterSettingValue.value,
                setting.masterSettingRemarks.value,
            )
        }

        val masterSettingReferResponse =
            MasterSettingReferResponse(
                status,
                message,
                MasterSettingReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(masterSettingReferResponse)
    }
}