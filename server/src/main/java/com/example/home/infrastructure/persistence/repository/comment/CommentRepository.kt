package com.example.home.infrastructure.persistence.repository.comment

import com.example.home.domain.comment.Comment
import com.example.home.domain.value_object.comment.Content
import com.example.home.domain.value_object.etc.Month
import com.example.home.domain.value_object.etc.Year
import com.example.home.domain.value_object.group.GroupsId

interface CommentRepository {
    fun refer(groupsId: GroupsId, yyyy: Year? = null, mm: Month? = null): List<Comment>
    fun referAll(): List<Comment>
    fun save(
        groupsId: GroupsId,
        yyyy: Year,
        mm: Month,
        content: Content
    ): Comment

    fun update(
        groupsId: GroupsId,
        yyyy: Year,
        mm: Month,
        content: Content
    ): Int

    fun delete(groupsId: GroupsId, yyyy: Year? = null, mm: Month? = null): Int
}