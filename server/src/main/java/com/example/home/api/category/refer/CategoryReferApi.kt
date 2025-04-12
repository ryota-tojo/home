package com.example.home.api.category.refer


import com.example.home.api.ErrorResponse
import com.example.home.api.category.refer.request.CategoryReferRequest
import com.example.home.api.category.refer.response.CategoryReferResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.category.CategoryService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CategoryReferApi(
    private val categoryService: CategoryService

) {
    companion object {
        const val API_PATH = "api/category/refer"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: CategoryReferRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestCategoryId = if (request.categoryId == 0) null else request.categoryId?.let { CategoryId(it) }
        val requestGroupsId = if (request.groupsId == "") null else request.groupsId?.let { GroupsId(it) }
        val requestCategoryNo = if (request.categoryNo == 0) null else request.categoryNo?.let { CategoryNo(it) }

        val requestOffset = request.offSet
        val requestLimit = request.limit

        val serviceExecResult = categoryService.refer(
            requestCategoryId, requestGroupsId, requestCategoryNo, requestOffset, requestLimit
        )

        // エラー時のレスポンス
        if (serviceExecResult.result != ResponseCode.成功.code) {

            val status = "error"
            val message = "カテゴリー参照失敗"
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
        val dataObject = serviceExecResult.category?.map { category ->
            CategoryReferResponse.CategoryObject(
                category.id.value,
                category.groupsId.value,
                category.categoryNo.value,
                category.categoryName.value,
                category.deleted.value,
            )
        }

        val categoryReferResponse =
            CategoryReferResponse(
                status,
                message,
                CategoryReferResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(categoryReferResponse)
    }
}