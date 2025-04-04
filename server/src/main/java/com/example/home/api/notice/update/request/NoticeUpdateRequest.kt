package com.example.home.api.notice.update.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class NoticeUpdateRequest(
    @JsonProperty("notice_id")
    @field:NotNull(message = "キー「notice_id」が存在しません")
    @field:NotBlank(message = "キー「notice_id」が未入力です")
    val noticeId: Int,

    @JsonProperty("title")
    @field:NotNull(message = "キー「title」が存在しません")
    @field:NotBlank(message = "キー「title」が未入力です")
    @field:Size(max = 1024, message = "キー「title」は1024桁以内で入力してください")
    val title: String,

    @JsonProperty("content")
    @field:NotNull(message = "キー「content」が存在しません")
    @field:NotBlank(message = "キー「content」が未入力です")
    val content: String,
)
