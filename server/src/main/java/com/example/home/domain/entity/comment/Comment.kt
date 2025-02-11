package com.example.home.domain.entity.comment

import com.example.home.domain.value_object.comment.Content
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId

data class Comment(
    val id: Int,
    val groupsId: GroupsId,
    val yyyy: YYYY,
    val mm: MM,
    val content: Content,
    val fixedFlg: FixedFlg
)
