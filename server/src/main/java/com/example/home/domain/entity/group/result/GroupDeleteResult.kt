package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupDeletionCounts

data class GroupDeleteResult(
    val result: String,
    val groupDeletionCounts: GroupDeletionCounts? = null
)
