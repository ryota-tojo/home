package com.example.home.api.template.search.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ShoppingSearchCreateRequest(
    @JsonProperty("groups_id")
    @field:NotBlank(message = "キー「groups_id」は必須です")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("template_no")
    val templateNo: Int,

    @JsonProperty("template_id")
    @field:NotBlank(message = "キー「template_id」は必須です")
    @field:Size(max = 64, message = "キー「template_id」は64桁以内で入力してください")
    val templateId: String,

    @JsonProperty("template_name")
    @field:NotBlank(message = "キー「template_name」は必須です")
    @field:Size(max = 64, message = "キー「template_name」は64桁以内で入力してください")
    val templateName: String,

    @JsonProperty("member_id")
    val memberId: Int? = null,

    @JsonProperty("category_id")
    val categoryId: Int? = null,

    @JsonProperty("type")
    val type: Int? = null,

    @JsonProperty("payment")
    val payment: Int? = null,

    @JsonProperty("settlement")
    val settlement: Int? = null,

    @JsonProperty("min_amount")
    val minAmount: Int? = null,

    @JsonProperty("max_amount")
    val maxAmount: Int? = null,

    @JsonProperty("remarks")
    @field:Size(max = 1024, message = "キー「remarks」は1024桁以内で入力してください")
    val remarks: String? = null,

    @JsonProperty("use")
    @field:NotNull(message = "キー「use」は必須です")
    val use: Int,
)


