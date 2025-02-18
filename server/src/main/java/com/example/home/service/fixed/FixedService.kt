package com.example.home.service.fixed

import com.example.home.domain.entity.fixed.FixedRefer
import com.example.home.domain.entity.fixed.result.FixedFixedResult
import com.example.home.domain.entity.fixed.result.FixedReferResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.repository.fixed.FixedRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import org.springframework.stereotype.Service

@Service
class FixedService(
    private val fixedRepository: FixedRepository,
    private val budgetsRepository: BudgetsRepository,
    private val shoppingRepository: ShoppingRepository,
    private val commentRepository: CommentRepository
) {

    fun refer(groupsId: GroupsId, yyyy: YYYY, mm: MM? = null): FixedReferResult {
        val fixedList = fixedRepository.refer(groupsId, yyyy, mm)
        val months = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        val fixedRefer = months.map { month ->
            val isFixed = fixedList.any { it.mm.value == month } // 修正ポイント
            FixedRefer(yyyy, MM(month), isFixed)
        }
        return FixedReferResult(
            ResponseCode.成功.code,
            fixedRefer
        )
    }

    fun fixed(groupsId: GroupsId, yyyy: YYYY, mm: MM): FixedFixedResult {
        val budgetsFixedRows = budgetsRepository.fixed(groupsId, yyyy, mm)
        val shoppingFixedRows = shoppingRepository.fixed(groupsId, yyyy, mm)
        val commentFixedRows = commentRepository.fixed(groupsId, yyyy, mm)
        fixedRepository.fixed(groupsId, yyyy, mm)
        return FixedFixedResult(
            ResponseCode.成功.code,
            budgetsFixedRows,
            shoppingFixedRows,
            commentFixedRows,
        )
    }

    fun unFixed(groupsId: GroupsId, yyyy: YYYY, mm: MM): FixedFixedResult {
        val budgetsUnFixedRows = budgetsRepository.unFixed(groupsId, yyyy, mm)
        val shoppingUnFixedRows = shoppingRepository.unFixed(groupsId, yyyy, mm)
        val commentUnFixedRows = commentRepository.unFixed(groupsId, yyyy, mm)
        fixedRepository.unFixed(groupsId, yyyy, mm)
        return FixedFixedResult(
            ResponseCode.成功.code,
            budgetsUnFixedRows,
            shoppingUnFixedRows,
            commentUnFixedRows,
        )
    }
}