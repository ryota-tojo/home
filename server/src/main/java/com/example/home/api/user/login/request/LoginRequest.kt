package com.example.home.api.user.login.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class LoginRequest(
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
)
