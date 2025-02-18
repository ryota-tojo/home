package com.example.home.domain.repository.fixed

import com.example.home.domain.entity.fixed.Fixed
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId

interface FixedRepository {
    fun refer(groupsId: GroupsId, yyyy: YYYY, mm: MM? = null): List<Fixed>
    fun fixed(groupsId: GroupsId, yyyy: YYYY, mm: MM): Boolean
    fun unFixed(groupsId: GroupsId, yyyy: YYYY, mm: MM): Boolean
}