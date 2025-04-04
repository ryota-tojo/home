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

    @JsonProperty("template_id")
    @field:NotBlank(message = "キー「template_id」は必須です")
    @field:Size(max = 64, message = "キー「template_id」は64桁以内で入力してください")
    val templateId: String,

    @JsonProperty("template_name")
    @field:NotBlank(message = "キー「template_name」は必須です")
    @field:Size(max = 64, message = "キー「template_name」は64桁以内で入力してください")
    val templateName: String,

    @JsonProperty("member_id")
    @field:NotNull(message = "キー「member_id」は必須です")
    val memberId: Int,

    @JsonProperty("category_id")
    @field:NotNull(message = "キー「category_id」は必須です")
    val categoryId: Int,

    @JsonProperty("type")
    @field:NotNull(message = "キー「type」は必須です")
    val type: Int,

    @JsonProperty("payment")
    @field:NotNull(message = "キー「payment」は必須です")
    val payment: Int,

    @JsonProperty("settlement")
    @field:NotNull(message = "キー「settlement」は必須です")
    val settlement: Int,

    @JsonProperty("amount")
    @field:NotNull(message = "キー「amount」は必須です")
    val amount: Int,

    @JsonProperty("remarks")
    @field:NotBlank(message = "キー「remarks」は必須です")
    @field:Size(max = 1024, message = "キー「remarks」は1024桁以内で入力してください")
    val remarks: String,

    @JsonProperty("use")
    @field:NotNull(message = "キー「use」は必須です")
    val use: Int,
)


