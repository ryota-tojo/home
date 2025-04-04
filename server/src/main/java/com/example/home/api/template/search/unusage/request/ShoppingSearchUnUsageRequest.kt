package com.example.home.api.template.search.unusage.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ShoppingSearchUnUsageRequest(
    @JsonProperty("groups_id")
    @field:NotBlank(message = "キー「groups_id」は必須です")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("template_id")
    @field:NotBlank(message = "キー「template_id」は必須です")
    @field:Size(max = 64, message = "キー「template_id」は64桁以内で入力してください")
    val templateId: String,

    )


