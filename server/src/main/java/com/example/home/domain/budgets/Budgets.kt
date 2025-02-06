package com.example.home.domain.budgets

import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.Month
import com.example.home.domain.value_object.etc.Year
import com.example.home.domain.value_object.group.GroupsId

data class Budgets(
    val id :Int,
    val groupsId: GroupsId,
    val year: Year,
    val month: Month,
    val categoryName: CategoryName,
    val amount  : Amount,
    val fixedFlg: FixedFlg
)