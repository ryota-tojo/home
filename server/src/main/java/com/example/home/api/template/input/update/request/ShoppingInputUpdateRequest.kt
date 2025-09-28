package com.example.home.api.template.input.update.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ShoppingInputUpdateRequest(
    @JsonProperty("groups_id")
    @field:NotBlank(message = "キー「groups_id」は必須です")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("template_no")
    val templateNo: Int? = null,

    @JsonProperty("template_id")
    @field:NotBlank(message = "キー「template_id」は必須です")
    @field:Size(max = 64, message = "キー「template_id」は64桁以内で入力してください")
    val templateId: String,

    @JsonProperty("template_name")
    @field:Size(max = 64, message = "キー「template_name」は64桁以内で入力してください")
    val templateName: String? = null,

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

    @JsonProperty("amount")
    val amount: Int? = null,

    @JsonProperty("remarks")
    @field:Size(max = 1024, message = "キー「remarks」は1024桁以内で入力してください")
    val remarks: String? = null,

    @JsonProperty("use")
    val use: Int? = null,
)


