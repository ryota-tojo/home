package com.example.home.api.category.create


import com.example.home.api.ErrorResponse
import com.example.home.api.category.create.request.CategoryCreateRequest
import com.example.home.api.category.create.response.CategoryCreateResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
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
class CategoryCreateApi(
    private val categoryService: CategoryService

) {
    companion object {
        const val API_PATH = "api/category/create"
    }

    @PostMapping(value = [API_PATH])
    fun create(
        @RequestBody @Valid request: CategoryCreateRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestCategoryNo = CategoryNo(request.categoryNo)
        val requestCategoryName = CategoryName(request.categoryName)

        val serviceExecResult = categoryService.save(
            requestGroupsId, requestCategoryNo, requestCategoryName
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "カテゴリー登録失敗"
            var parameter = "-"
            var errorMessage = "想定外のエラー"

            if (serviceExecResult.result == ResponseCode.バリデーションエラー.code) {
                parameter = "-"
                errorMessage = ResponseCode.バリデーションエラー.message
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
        val dataObject = CategoryCreateResponse.CategoryObject(
            serviceExecResult.category?.id?.value,
            serviceExecResult.category?.groupsId?.value,
            serviceExecResult.category?.categoryNo?.value,
            serviceExecResult.category?.categoryName?.value,
        )

        val categoryCreateResponse =
            CategoryCreateResponse(
                status,
                message,
                CategoryCreateResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(categoryCreateResponse)
    }
}