package com.example.home.api.group.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class GroupReferRequest(
    @JsonProperty("groups_id")
    @field:NotBlank(message = "GroupsID cannot be blank")
    @field:Size(max = 64, message = "GroupsID must be 64 characters")
    val groupsId: String? = null
)
