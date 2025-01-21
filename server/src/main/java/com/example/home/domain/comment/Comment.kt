package com.example.home.domain.comment

import com.example.home.domain.value_object.comment.Content
import com.example.home.domain.value_object.etc.Month
import com.example.home.domain.value_object.etc.Year
import com.example.home.domain.value_object.group.GroupsId

data class Comment(
    val id: Int,
    val groupsId: GroupsId,
    val yyyy: Year,
    val mm: Month,
    val content: Content
)
