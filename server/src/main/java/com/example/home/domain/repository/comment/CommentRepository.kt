package com.example.home.domain.repository.comment

import com.example.home.domain.entity.comment.Comment
import com.example.home.domain.value_object.comment.Content
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId

interface CommentRepository {
    fun refer(groupsId: GroupsId, yyyy: YYYY? = null, mm: MM? = null): List<Comment>
    fun referAll(): List<Comment>
    fun save(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM,
        content: Content
    ): Comment

    fun update(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM,
        content: Content
    ): Int

    fun delete(groupsId: GroupsId, yyyy: YYYY? = null, mm: MM? = null): Int
}