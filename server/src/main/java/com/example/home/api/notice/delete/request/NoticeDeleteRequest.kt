package com.example.home.api.notice.delete.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class NoticeDeleteRequest(
    @JsonProperty("notice_id")
    @field:NotNull(message = "キー「notice_id」が存在しません")
    val noticeId: Int,
)