package com.example.home.api.comment.refer.response

import com.fasterxml.jackson.annotation.JsonProperty

data class CommentReferResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("comment_list")
        val commentList: List<CommentObject>? = null
    )

    data class CommentObject(
        @JsonProperty("comment_id")
        val categoryId: Int? = null,

        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("yyyy")
        val yyyy: Int? = null,

        @JsonProperty("mm")
        val mm: Int? = null,

        @JsonProperty("content")
        val content: String? = null,

        @JsonProperty("fixed")
        val fixed: Int? = null,
    )

}