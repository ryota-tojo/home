package com.example.home.api.shopping.get_all_categories


import com.example.home.api.shopping.get_all_categories.request.ShoppingGetAllCategoryRequest
import com.example.home.api.shopping.get_all_categories.response.ShoppingGetAllCategoryResponse
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.service.shopping.ShoppingService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingGetAllCategoriesApi(
    private val shoppingService: ShoppingService

) {
    companion object {
        const val API_PATH = "api/shopping/all_category"
    }

    @GetMapping(value = [API_PATH])
    fun refer(
        @RequestBody @Valid request: ShoppingGetAllCategoryRequest,
        response: HttpServletResponse
    ): ResponseEntity<Any> {

        // リクエスト取得
        val requestGroupsId = GroupsId(request.groupsId)
        val requestYYYY = if (request.yyyy == null) null else request.yyyy.let { YYYY(it) }
        val requestMM = if (request.mm == null) null else request.mm.let { MM(it) }

        val serviceExecResult = shoppingService.getAllCategories(
            requestGroupsId,
            requestYYYY,
            requestMM,
        )

        // 成功時のレスポンス
        val status = "success"
        val message = ResponseCode.成功.message
        val dataObject = serviceExecResult.map { category ->
            ShoppingGetAllCategoryResponse.CategoryObject(
                category.id.value,
                category.groupsId.value,
                category.categoryNo.value,
                category.categoryName.value,
            )
        }

        val shoppingGetAllCategoryResponse =
            ShoppingGetAllCategoryResponse(
                status,
                message,
                ShoppingGetAllCategoryResponse.DataObject(dataObject)
            )
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(shoppingGetAllCategoryResponse)
    }
}