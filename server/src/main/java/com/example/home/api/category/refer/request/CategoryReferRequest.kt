package com.example.home.api.category.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class CategoryReferRequest(
    @JsonProperty("category_id")
    val categoryId: Int? = null,

    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String? = null,

    @JsonProperty("category_no")
    val categoryNo: Int? = null,

    @JsonProperty("offset")
    val offSet: Long? = null,

    @JsonProperty("limit")
    val limit: Int? = null,
    )
