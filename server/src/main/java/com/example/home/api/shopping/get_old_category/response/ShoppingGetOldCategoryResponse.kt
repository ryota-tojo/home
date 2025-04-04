package com.example.home.api.shopping.get_old_category.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ShoppingGetOldCategoryResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("category_list")
        val categoryList: List<CategoryObject>? = null
    )

    data class CategoryObject(
        @JsonProperty("category_id")
        val categoryId: Int? = null,

        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("category_no")
        val categoryNo: Int? = null,

        @JsonProperty("category_name")
        val categoryName: String? = null,

        )
}