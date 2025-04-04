package com.example.home.api.comment.refer.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Size

data class CommentReferRequest(
    @JsonProperty("groups_id")
    @field:Size(max = 64, message = "キー「groups_id」は64桁以内で入力してください")
    val groupsId: String,

    @JsonProperty("yyyy")
    @field:Size(max = 4, message = "キー「yyyy」は4桁以内で入力してください")
    val yyyy: Int,

    @JsonProperty("mm")
    @field:Size(max = 2, message = "キー「mm」は2桁以内で入力してください")
    val mm: Int,

    )
