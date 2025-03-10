package com.example.home.service.analysis

import com.example.home.domain.entity.analysis.AnalysisPA
import com.example.home.domain.entity.analysis.AnalysisPACategory
import com.example.home.domain.entity.analysis.AnalysisPAData
import com.example.home.domain.entity.analysis.AnalysisPATotal
import com.example.home.domain.entity.analysis.result.AnalysisPAResult
import com.example.home.domain.entity.budgets.Budgets
import com.example.home.domain.entity.category.Category
import com.example.home.domain.entity.shopping.Shopping
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.fixed.FixedRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.analysis.UseRate
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import org.springframework.stereotype.Service

@Service
class AnalysisPAService(
    private val categoryRepository: CategoryRepository,
    private val budgetsRepository: BudgetsRepository,
    private val shoppingRepository: ShoppingRepository,
    private val fixedRepository: FixedRepository,
) {
    fun analysisPA(
        groupsId: GroupsId,
        yyyy: YYYY,
    ): AnalysisPAResult {

        val month: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        // 年間の購入データを取得
        val shoppingList = shoppingRepository.refer(
            groupsId = groupsId,
            shoppingDateYYYY = yyyy
        )

        // 予算取得
        val budgetsList = budgetsRepository.refer(groupsId, yyyy)

        // カテゴリーを取得
        val categoryIdList = shoppingRepository.getAllCategories(groupsId, yyyy)
        val categoryList =
            categoryIdList.map { id -> categoryRepository.refer(categoryId = id).firstOrNull() }.filterNotNull()

        // 月別に購入データを絞り込む
        val analysisPACategory = categoryList.map { category ->

            val analysisPAMonthData = month.map { month ->
                getAnalysisPACategoryData(
                    groupsId,
                    yyyy,
                    shoppingList,
                    budgetsList,
                    category,
                    MM(month),
                    month.toString()
                )
            }
            val analysisPAYearData = getAnalysisPACategoryData(
                groupsId,
                yyyy,
                shoppingList,
                budgetsList,
                category,
                null,
                "year"
            )

            AnalysisPACategory(
                category.id,
                category.categoryName,
                analysisPAMonthData,
                analysisPAYearData
            )
        }

        // 総計
        val analysisPATotalData = getAnalysisPATotalData(
            groupsId,
            yyyy,
            shoppingList,
            budgetsList,
            "total"
        )
        val analysisPATotal = AnalysisPATotal(analysisPATotalData)

        return AnalysisPAResult(
            ResponseCode.成功.code,
            AnalysisPA(
                analysisPACategory,
                analysisPATotal
            )
        )

    }

    fun getAnalysisPACategoryData(
        groupsId: GroupsId,
        yyyy: YYYY,
        shoppingList: List<Shopping>,
        budgetsList: List<Budgets>,
        category: Category,
        mm: MM? = null,
        timeFrame: String
    ): AnalysisPAData {

        var filteringShoppingList: List<Shopping> = listOf()
        var budgetsAmount: Int = 0

        if (mm != null) {
            // 購入データをカテゴリー＋月で絞りこむ
            filteringShoppingList =
                shoppingList.filter { it.categoryId == category.id && it.shoppingDate.month.value == mm.value }
            // 予算をカテゴリー＋月で絞りこむ
            budgetsAmount =
                budgetsList.filter { it.categoryId == category.id && it.MM.value == mm.value }.sumOf { it.amount.value }
        } else {
            // 購入データをカテゴリー＋月で絞りこむ
            filteringShoppingList = shoppingList.filter { it.categoryId == category.id }
            // 予算をカテゴリー＋月で絞りこむ
            budgetsAmount = budgetsList.filter { it.categoryId == category.id }.sumOf { it.amount.value }
        }

        // 購入データから出費を算出
        val shoppingAmountExpense =
            filteringShoppingList.filter { it.shoppingType.value == 1 }.sumOf { it.shoppingAmount.value }
        // 購入データから収入を算出
        val shoppingAmountIncome =
            filteringShoppingList.filter { it.shoppingType.value == 0 }.sumOf { it.shoppingAmount.value }

        // 使用金額 - 支出
        val expenseAmount = Amount(shoppingAmountExpense)
        // 使用金額 - 収入＋予算
        val incomeAndBudgetsAmount = Amount(shoppingAmountIncome + budgetsAmount)
        // 平均
        val averageAmount = getAverageData(
            groupsId,
            yyyy,
            shoppingList,
            category
        )
        // 使用率
        var useRate = UseRate(0.0)
        if (incomeAndBudgetsAmount.value == 0) {
            useRate = UseRate(0.0)
        } else {
            val useRateString =
                String.format("%.2f", (expenseAmount.value.toDouble() / incomeAndBudgetsAmount.value.toDouble() * 100))
            useRate = UseRate(useRateString.toDouble())
        }

        // 残金
        val balanceAmount = Amount(incomeAndBudgetsAmount.value - expenseAmount.value)

        return AnalysisPAData(
            timeFrame,
            expenseAmount,
            incomeAndBudgetsAmount,
            averageAmount,
            useRate,
            balanceAmount,
        )
    }

    fun getAnalysisPATotalData(
        groupsId: GroupsId,
        yyyy: YYYY,
        shoppingList: List<Shopping>,
        budgetsList: List<Budgets>,
        timeFrame: String
    ): AnalysisPAData {

        // 購入データから出費合計を算出
        val shoppingAmountExpense =
            shoppingList.filter { it.shoppingType.value == 1 }.sumOf { it.shoppingAmount.value }
        // 購入データから収入合計を算出
        val shoppingAmountIncome =
            shoppingList.filter { it.shoppingType.value == 0 }.sumOf { it.shoppingAmount.value }
        // 予算データから予算合計金額を算出
        val budgetsAmount =
            budgetsList.sumOf { it.amount.value }

        // 使用金額 - 支出
        val expenseAmount = Amount(shoppingAmountExpense)
        // 使用金額 - 収入＋予算
        val incomeAndBudgetsAmount = Amount(shoppingAmountIncome + budgetsAmount)
        // 平均
        val averageAmount = getAverageData(
            groupsId,
            yyyy,
            shoppingList
        )
        // 使用率
        var useRate = UseRate(0.0)
        if (incomeAndBudgetsAmount.value == 0) {
            useRate = UseRate(0.0)
        } else {
            val useRateString =
                String.format("%.2f", (expenseAmount.value.toDouble() / incomeAndBudgetsAmount.value.toDouble() * 100))
            useRate = UseRate(useRateString.toDouble())
        }

        // 残金
        val balanceAmount = Amount(incomeAndBudgetsAmount.value - expenseAmount.value)

        return AnalysisPAData(
            timeFrame,
            expenseAmount,
            incomeAndBudgetsAmount,
            averageAmount,
            useRate,
            balanceAmount,
        )
    }

    fun getAverageData(
        groupsId: GroupsId,
        yyyy: YYYY,
        shoppingList: List<Shopping>,
        category: Category? = null
    ): Amount {

        val fixedList = fixedRepository.refer(groupsId, yyyy)
        val fixedMonthList = fixedList.map { fixed ->
            fixed.mm
        }
        val fixedMonthCount = fixedMonthList.size

        var fixedShoppingAmountList: List<Int> = listOf()
        if (category != null) {
            fixedShoppingAmountList = fixedMonthList.map { month ->
                val expenseAmount =
                    shoppingList.filter { it.categoryId == category.id && it.shoppingDate.month.value == month.value && it.shoppingType.value == 1 }
                        .sumOf { it.shoppingAmount.value }
                val incomeAmount =
                    shoppingList.filter { it.categoryId == category.id && it.shoppingDate.month.value == month.value && it.shoppingType.value == 0 }
                        .sumOf { it.shoppingAmount.value }
                expenseAmount - incomeAmount
            }
        } else {
            fixedShoppingAmountList = fixedMonthList.map { month ->
                val expenseAmount =
                    shoppingList.filter { it.shoppingDate.month.value == month.value && it.shoppingType.value == 1 }
                        .sumOf { it.shoppingAmount.value }
                val incomeAmount =
                    shoppingList.filter { it.shoppingDate.month.value == month.value && it.shoppingType.value == 0 }
                        .sumOf { it.shoppingAmount.value }
                expenseAmount - incomeAmount
            }
        }

        return Amount(fixedShoppingAmountList.sum() / fixedMonthCount)

    }
}