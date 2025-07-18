package com.example.home.api.master.screen.create.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class MasterScreenCreateRequest(
    @JsonProperty("screen_id")
    @field:NotNull(message = "キー「screen_id」が存在しません")
    @field:NotBlank(message = "キー「screen_id」が未入力です")
    @field:Size(max = 256, message = "キー「screen_id」は512桁以内で入力してください")
    val screenId: String,

    @JsonProperty("screen_name")
    @field:NotNull(message = "キー「screen_name」が存在しません")
    @field:NotBlank(message = "キー「screen_name」が未入力です")
    @field:Size(max = 256, message = "キー「screen_id」は512桁以内で入力してください")
    val screenName: String,

    @JsonProperty("remarks")
    @field:NotNull(message = "キー「remarks」が存在しません")
    @field:Size(max = 1024, message = "キー「remarks」は1024桁以内で入力してください")
    val remarks: String,

    )
