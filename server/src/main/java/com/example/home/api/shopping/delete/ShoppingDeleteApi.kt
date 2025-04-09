package com.example.home.api.shopping.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.shopping.delete.request.ShoppingDeleteRequest
import com.example.home.api.shopping.delete.response.ShoppingDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.shopping.ShoppingId
import com.example.home.service.shopping.ShoppingService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingDeleteApi(
    private val shoppingService: ShoppingService

) {
    companion object {
        const val API_PATH = "api/shopping/delete"
    }

    @PostMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestShoppingId = request.shoppingId?.let { ShoppingId(it) }
        val requestGroupsId = request.groupsId?.takeIf { it.isNotBlank() }?.let { GroupsId(it) }

        val requestYYYY = request.yyyy?.let { YYYY(it) }
        val requestMM = request.mm?.let { MM(it) }

        val serviceExecResult = shoppingService.delete(
            requestShoppingId,
            requestGroupsId,
            requestYYYY,
            requestMM
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "購入データ削除失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.データ不正エラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.データ不正エラー.message
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

        val shoppingDeleteResponse =
            ShoppingDeleteResponse(
                status,
                message,
                ShoppingDeleteResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingDeleteResponse)
    }
}