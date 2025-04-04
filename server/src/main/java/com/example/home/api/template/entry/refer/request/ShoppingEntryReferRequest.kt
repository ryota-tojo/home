package com.example.home.api.template.entry.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class ShoppingEntryReferRequest(
    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String? = null,

    @JsonProperty("template_id")
    @field:Size(max = 64, message = "キー「template_id」は64桁以内で入力してください")
    val templateId: String? = null,

    )
