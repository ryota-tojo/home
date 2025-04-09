package com.example.home.api.master.setting.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.master.setting.delete.request.MasterSettingDeleteRequest
import com.example.home.api.master.setting.delete.response.MasterSettingDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.service.master.MasterSettingService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MasterSettingDeleteApi(
    private val masterSettingService: MasterSettingService

) {
    companion object {
        const val API_PATH = "api/master/setting/delete"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: MasterSettingDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestKey = MasterSettingKey(request.settingKey)

        val serviceExecResult = masterSettingService.delete(
            requestKey
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "マスタ設定削除失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

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
        val dataObject = serviceExecResult.deleteRows

        val masterSettingDeleteResponse =
            MasterSettingDeleteResponse(
                status,
                message,
                MasterSettingDeleteResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(masterSettingDeleteResponse)
    }
}