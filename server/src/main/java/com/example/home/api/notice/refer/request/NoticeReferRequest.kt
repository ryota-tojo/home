package com.example.home.api.notice.refer.request

import com.fasterxml.jackson.annotation.JsonProperty

data class NoticeReferRequest(
    @JsonProperty("notice_id")
    val noticeId: Int,

    @JsonProperty("offset")
    val offSet: Long? = null,

    @JsonProperty("limit")
    val limit: Int? = null,
    )
