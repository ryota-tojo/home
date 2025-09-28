package com.example.home.api.category.delete


import com.example.home.api.ErrorResponse
import com.example.home.api.category.delete.request.CategoryDeleteRequest
import com.example.home.api.category.delete.response.CategoryDeleteResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.category.CategoryService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryDeleteApi(
    private val categoryService: CategoryService

) {
    companion object {
        const val API_PATH = "api/category/delete"
    }

    @PostMapping(value = [API_PATH])
    fun delete(
        @RequestBody @Valid request: CategoryDeleteRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestCategoryId = if (request.categoryId == 0) null else request.categoryId?.let { CategoryId(it) }

        val serviceExecResult = categoryService.delete(
            requestGroupsId, requestCategoryId
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "カテゴリー削除失敗"
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
        val dataObject = serviceExecResult.deleteRows

        val categoryDeleteResponse =
            CategoryDeleteResponse(
                status,
                message,
                CategoryDeleteResponse.DataObject(dataObject!!)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(categoryDeleteResponse)
    }
}