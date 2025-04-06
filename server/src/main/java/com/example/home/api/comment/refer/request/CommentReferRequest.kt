package com.example.home.api.comment.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class CommentReferRequest(
    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String? = null,

    @JsonProperty("yyyy")
    @field:Min(1901, message = "キー「yyyy」は1901以上を入力してください")
    @field:Max(2100, message = "キー「yyyy」は2100以下を入力してください")
    val yyyy: Int? = null,

    @JsonProperty("mm")
    @field:Min(1, message = "キー「mm」は1以上を入力してください")
    @field:Max(12, message = "キー「mm」は12以下を入力してください")
    val mm: Int? = null,

    )
