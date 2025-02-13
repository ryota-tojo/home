package com.example.home.domain.repository.budgets

import com.example.home.domain.entity.budgets.Budgets
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId

interface BudgetsRepository {
    fun refer(groupsId: GroupsId, yyyy: YYYY? = null, mm: MM? = null, categoryNo: CategoryId? = null): List<Budgets>
    fun save(groupsId: GroupsId, yyyy: YYYY, mm: MM, categoryNo: CategoryId, amount: Amount): Budgets
    fun update(groupsId: GroupsId, yyyy: YYYY, mm: MM, categoryNo: CategoryId, amount: Amount, fixedFlg: FixedFlg): Int
    fun delete(groupsId: GroupsId, yyyy: YYYY? = null, mm: MM? = null, categoryNo: CategoryId? = null): Int
}