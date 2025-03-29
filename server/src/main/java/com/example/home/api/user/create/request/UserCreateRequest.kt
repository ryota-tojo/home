package com.example.home.api.user.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UserCreateRequest(
    @JsonProperty("user_name")
    @field:NotNull(message = "キー「user_name」が存在しません")
    @field:NotBlank(message = "キー「user_name」が未入力です")
    @field:Size(max = 64, message = "キー「user_name」は64桁以内で入力してください")
    val userName: String,

    @JsonProperty("password")
    @field:NotNull(message = "キー「password」が存在しません")
    @field:NotBlank(message = "キー「password」が未入力です")
    @field:Size(max = 64, message = "キー「password」は64桁以内で入力してください")
    val password: String,

    @JsonProperty("permission")
    @field:NotNull(message = "キー「permission」が存在しません")
    val permission: Int,

    @JsonProperty("approval")
    @field:NotNull(message = "キー「approval」が存在しません")
    val approval: Int,

    @JsonProperty("delete")
    @field:NotNull(message = "キー「delete」が存在しません")
    val delete: Int,
)

