package com.example.home.domain.entity.fixed

import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.fixed.FixedId
import com.example.home.domain.value_object.group.GroupsId

data class Fixed(
    val id: FixedId,
    val groupsId: GroupsId,
    val yyyy: YYYY,
    val mm: MM
)