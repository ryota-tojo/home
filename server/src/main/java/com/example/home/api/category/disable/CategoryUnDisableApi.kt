package com.example.home.api.category.disable


import com.example.home.api.ErrorResponse
import com.example.home.api.category.disable.request.CategoryDisableRequest
import com.example.home.api.category.disable.response.CategoryDisableResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.service.category.CategoryService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryUnDisableApi(
    private val categoryService: CategoryService

) {
    companion object {
        const val API_PATH = "api/category/un-disable"
    }

    @PostMapping(value = [API_PATH])
    fun disable(
        @RequestBody @Valid request: CategoryDisableRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestCategoryId = CategoryId(request.categoryId)

        val serviceExecResult = categoryService.setUnDeleted(requestCategoryId)

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "カテゴリー有効化失敗"
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
        val dataObject = serviceExecResult.updateRows

        val categoryDisableResponse =
            CategoryDisableResponse(
                status,
                message,
                CategoryDisableResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(categoryDisableResponse)
    }
}