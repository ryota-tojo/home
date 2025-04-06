package com.example.home.api.member.create.response

import com.fasterxml.jackson.annotation.JsonProperty

data class MemberCreateResponse(
    @JsonProperty("status")
    val status: String,

    @JsonProperty("message")
    val message: String,

    @JsonProperty("data")
    val data: DataObject

) {
    data class DataObject(
        @JsonProperty("member")
        val member: MemberObject? = null
    )

    data class MemberObject(
        @JsonProperty("member_id")
        val memberId: Int? = null,

        @JsonProperty("groups_id")
        val groupsId: String? = null,

        @JsonProperty("member_no")
        val memberNo: Int? = null,

        @JsonProperty("member_name")
        val memberName: String? = null,
    )
}