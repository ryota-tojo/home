package com.example.home.api.shopping.get_all_categories.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class ShoppingGetAllMemberRequest(
    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("yyyy")
    @field:Min(value = 1000, message = "キー「yyyy」は4桁の年を入力してください")
    @field:Max(value = 9999, message = "キー「yyyy」は4桁の年を入力してください")
    val yyyy: Int? = null,

    @JsonProperty("mm")
    @field:Min(value = 1, message = "キー「mm」は1以上の値を入力してください")
    @field:Max(value = 12, message = "キー「mm」は12以下の値を入力してください")
    val mm: Int? = null,
)
