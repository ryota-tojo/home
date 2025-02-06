package com.example.home.domain.group.result

import com.example.home.domain.group.GroupList
import javax.sql.RowSet

data class GroupListUpdateResult(
    val result: String,
    val updateRowSet: Int? = 0
)
