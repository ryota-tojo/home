package com.example.home.api.user.update.info.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserInfoUpdateRequest(
    @JsonProperty("user_id")
    @field:NotNull(message = "キー「user_id」が存在しません")
    val userId: Int,

    @JsonProperty("user_name")
    @field:Size(max = 64, message = "キー「user_name」は64桁以内で入力してください")
    val userName: String? = null,

    @JsonProperty("password")
    @field:Size(max = 64, message = "キー「password」は64桁以内で入力してください")
    val password: String? = null,

    @JsonProperty("permission")
    val permission: Int? = null,

    @JsonProperty("approval")
    val approval: Int? = null,

    @JsonProperty("delete")
    val delete: Int? = null,
)
