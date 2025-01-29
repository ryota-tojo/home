package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupList
import javax.sql.RowSet

data class GroupListUpdateResult(
    val result: String,
    val updateRow: Int? = 0
)
