package com.example.home.service.report

import com.example.home.domain.entity.report.*
import com.example.home.domain.entity.report.result.ReportResult
import com.example.home.domain.entity.shopping.Shopping
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupPassword
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val groupListRepository: GroupListRepository,
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val budgetsRepository: BudgetsRepository,
    private val shoppingRepository: ShoppingRepository,
) {
    fun createReport(
        groupsId: GroupsId,
        groupPassword: GroupPassword,
        yyyy: YYYY,
        mm: MM
    ): ReportResult {

        if (!groupListRepository.certification(groupsId, groupPassword)) {
            return ReportResult(
                ResponseCode.グループ認証エラー.code,
                null
            )
        }

        val settlementReport = createSettlementReport(groupsId, yyyy, mm)
        val balanceReport = createBalanceReport(groupsId, yyyy, mm)

        val report = Report(
            yyyy,
            mm,
            settlementReport,
            balanceReport
        )

        // レスポンス
        return ReportResult(
            "成功",
            report
        )
    }

    private fun createSettlementReport(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM
    ): SettlementReport {
        // メンバー一覧取得
        val memberIdList = shoppingRepository.getAllMembers(groupsId, yyyy, mm)
        val memberList = memberIdList.map { id -> memberRepository.refer(memberId = id).firstOrNull() }.filterNotNull()

        // 未精算購入データを取得
        val unSettlementShoppingList: MutableList<Shopping> = mutableListOf()
        memberList.forEach { member ->
            unSettlementShoppingList.addAll(
                shoppingRepository.refer(
                    groupsId = groupsId,
                    shoppingDateYYYY = yyyy,
                    shoppingDateMM = mm,
                    memberId = member.id,
                    settlement = ShoppingSettlement(0)
                )
            )
        }

        // 未精算購入データ件数を取得
        val shoppingCount = unSettlementShoppingList.count()

        // メンバーごとの未精算金額を取得
        val memberAndAmount =
            memberList.map { member ->
                val amount = unSettlementShoppingList.sumOf { it.shoppingAmount.value }
                MemberAndAmount(member, Amount(amount))
            }

        // 未精算購入データ合計金額を取得
        // 出費
        val unSettlementShoppingAmountExpense = unSettlementShoppingList
            .filter { it.shoppingType.value == 1 }
            .sumOf { it.shoppingAmount.value }
        // 収入
        val unSettlementShoppingAmountIncome = unSettlementShoppingList
            .filter { it.shoppingType.value == 0 }
            .sumOf { it.shoppingAmount.value }
        // 合計
        val unSettlementShoppingAmountTotal =
            Amount(unSettlementShoppingAmountExpense.minus(unSettlementShoppingAmountIncome))

        // 精算レポート
        return SettlementReport(
            shoppingCount,
            memberAndAmount,
            unSettlementShoppingAmountTotal
        )
    }

    private fun createBalanceReport(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM
    ): BalanceReport {
        // カテゴリー一覧取得
        val categoryIdList = shoppingRepository.getAllCategories(groupsId, yyyy, mm)
        val categoryList =
            categoryIdList.map { id -> categoryRepository.refer(categoryId = id).firstOrNull() }.filterNotNull()

        // 予算取得
        val budgetsList = budgetsRepository.refer(groupsId, yyyy, mm)

        // 購入データ取得
        val shoppingList = shoppingRepository.refer(
            groupsId = groupsId,
            shoppingDateYYYY = yyyy,
            shoppingDateMM = mm
        )

        // カテゴリー別にマッピング
        val categoryAndAmountList = categoryList.map { category ->
            // 対象カテゴリーの予算を取得
            val tgtBudgets = budgetsList.filter { budget -> budget.categoryId == category.id }.firstOrNull()
            var budgets = 0
            if (tgtBudgets != null) {
                budgets = tgtBudgets.amount.value
            }

            // 対象購入データの出費合計金額を取得
            val analysisCategoryShoppingAmountExpense = shoppingList
                .filter { shopping -> shopping.categoryId == category.id && shopping.shoppingType.value == 1 }
                .sumOf { it.shoppingAmount.value }

            // 対象購入データの収入合計金額を取得
            val analysisCategoryShoppingAmountIncome = shoppingList
                .filter { shopping -> shopping.categoryId == category.id && shopping.shoppingType.value == 0 }
                .sumOf { it.shoppingAmount.value }

            // カテゴリーごとの残高を生成
            CategoryAndAmount(
                category = category,
                Amount(
                    // 購入データの残高を算出　（予算＋収入）-出費
                    (budgets + analysisCategoryShoppingAmountIncome) - analysisCategoryShoppingAmountExpense
                )
            )
        }

        // 合計
        // 予算合計
        val budgetsTotal = budgetsList.sumOf { it.amount.value }

        // 購入データ出費合計
        val analysisShoppingAmountExpense = shoppingList
            .filter { shopping -> shopping.shoppingType.value == 1 }
            .sumOf { it.shoppingAmount.value }

        // 購入データ収入合計
        val analysisShoppingAmountIncome = shoppingList
            .filter { shopping -> shopping.shoppingType.value == 0 }
            .sumOf { it.shoppingAmount.value }

        // 残高合計
        val analysisShoppingAmountTotal =
            Amount((budgetsTotal + analysisShoppingAmountIncome) - analysisShoppingAmountExpense)

        // 残高レポート
        return BalanceReport(
            categoryAndAmountList,
            analysisShoppingAmountTotal
        )
    }
}