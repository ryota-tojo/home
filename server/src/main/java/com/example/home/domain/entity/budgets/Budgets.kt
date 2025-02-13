package com.example.home.domain.entity.budgets

import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId

data class Budgets(
    val id: Int,
    val groupsId: GroupsId,
    val YYYY: YYYY,
    val MM: MM,
    val categoryId: CategoryId,
    val amount: Amount,
    val fixedFlg: FixedFlg
)