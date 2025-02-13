package com.example.home.service.budgets

import com.example.home.domain.entity.budgets.result.BudgetsDeleteResult
import com.example.home.domain.entity.budgets.result.BudgetsReferResult
import com.example.home.domain.entity.budgets.result.BudgetsSaveResult
import com.example.home.domain.entity.budgets.result.BudgetsUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import org.springframework.stereotype.Service

@Service
class BudgetsService(
    private val budgetsRepository: BudgetsRepository
) {
    fun refer(
        groupsId: GroupsId,
        yyyy: YYYY? = null,
        mm: MM? = null,
        categoryId: CategoryId? = null
    ): BudgetsReferResult {
        val budgetsList = budgetsRepository.refer(groupsId, yyyy, mm, categoryId)
        return BudgetsReferResult(
            ResponseCode.成功.code,
            budgetsList
        )
    }

    fun save(groupsId: GroupsId, yyyy: YYYY, mm: MM, categoryId: CategoryId, amount: Amount): BudgetsSaveResult {
        val budgetsList = budgetsRepository.refer(groupsId, yyyy, mm, categoryId)
        if (budgetsList != null) {
            return BudgetsSaveResult(
                ResponseCode.重複エラー.code,
                null
            )
        }
        val budgets = budgetsRepository.save(groupsId, yyyy, mm, categoryId, amount)
        return BudgetsSaveResult(
            ResponseCode.成功.code,
            budgets
        )
    }

    fun update(
        groupsId: GroupsId,
        yyyy: YYYY? = null,
        mm: MM? = null,
        categoryId: CategoryId? = null,
        amount: Amount? = null,
        fixedFlg: FixedFlg? = null
    ): BudgetsUpdateResult {
        val updateRows = budgetsRepository.update(groupsId, yyyy, mm, categoryId, amount, fixedFlg)
        if (updateRows == 0) {
            return BudgetsUpdateResult(
                ResponseCode.データ不在エラー.code,
                updateRows
            )
        }
        return BudgetsUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(
        groupsId: GroupsId,
        yyyy: YYYY? = null,
        mm: MM? = null,
        categoryId: CategoryId? = null
    ): BudgetsDeleteResult {
        val deleteRows = budgetsRepository.delete(groupsId, yyyy, mm, categoryId)
        if (deleteRows == 0) {
            return BudgetsDeleteResult(
                ResponseCode.データ不在エラー.code,
                deleteRows
            )
        }
        return BudgetsDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }
}