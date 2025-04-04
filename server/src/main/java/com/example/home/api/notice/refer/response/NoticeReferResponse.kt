package com.example.home.api.notice.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class NoticeReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("notice_list")
        val noticeList: List<NoticeObject>? = null
    )

    data class NoticeObject(
        @JsonProperty("notice_id")
        val noticeId: Int? = null,

        @JsonProperty("title")
        val title: String? = null,

        @JsonProperty("content")
        val content: String? = null,

        @JsonProperty("create_datetime")
        val createDatetime: String? = null,

        @JsonProperty("update_datetime")
        val updateDatetime: String? = null,

        )

}