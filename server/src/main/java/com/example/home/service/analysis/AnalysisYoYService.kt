package com.example.home.service.analysis

import com.example.home.domain.entity.analysis.AnalysisYoY
import com.example.home.domain.entity.analysis.AnalysisYoYCategory
import com.example.home.domain.entity.analysis.AnalysisYoYData
import com.example.home.domain.entity.analysis.AnalysisYoYTotal
import com.example.home.domain.entity.analysis.result.AnalysisYoYResult
import com.example.home.domain.entity.category.Category
import com.example.home.domain.entity.shopping.Shopping
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.value_object.analysis.Ratio
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.shopping.ShoppingType
import org.springframework.stereotype.Service

@Service
class AnalysisYoYService(
    private val categoryRepository: CategoryRepository,
    private val shoppingRepository: ShoppingRepository,
) {
    fun analysisYoY(
        groupsId: GroupsId,
        yyyy: YYYY,
    ): AnalysisYoYResult {

        val month: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        // 前年の購入データを取得
        val lastYyyy = YYYY(yyyy.value - 1)
        val lastShoppingList = shoppingRepository.refer(
            groupsId = groupsId,
            shoppingDateYYYY = lastYyyy,
            type = ShoppingType(1)
        )

        // 対象年の購入データを取得
        val thisShoppingList = shoppingRepository.refer(
            groupsId = groupsId,
            shoppingDateYYYY = yyyy,
            type = ShoppingType(1)
        )

        // カテゴリーを取得
        val categoryIdList = shoppingRepository.getAllCategories(groupsId, yyyy)
        val categoryList =
            categoryIdList.map { id -> categoryRepository.refer(categoryId = id).firstOrNull() }.filterNotNull()

        // 月別に購入データを絞り込む
        val analysisYoYCategory = categoryList.map { category ->

            // カテゴリー＋月別
            val analysisYoYMonthData = month.map { month ->
                getAnalysisYoYCategoryData(
                    lastShoppingList,
                    thisShoppingList,
                    category,
                    MM(month),
                    month.toString()
                )
            }
            // カテゴリー＋平均
            val analysisYoYAverageData = getAnalysisYoYCategoryAverageData(
                lastShoppingList,
                thisShoppingList,
                category,
                "average"
            )

            // カテゴリー＋年間
            val analysisYoYYearData = getAnalysisYoYCategoryData(
                lastShoppingList,
                thisShoppingList,
                category,
                null,
                "year"
            )

            AnalysisYoYCategory(
                category.id,
                category.categoryName,
                analysisYoYMonthData,
                analysisYoYAverageData,
                analysisYoYYearData
            )
        }

        // 総計
        // 月別に購入データを絞り込む
        // カテゴリー＋月別
        val analysisYoYTotalMonthData = month.map { month ->
            getAnalysisYoYTotalData(
                lastShoppingList,
                thisShoppingList,
                MM(month),
                month.toString()
            )
        }
        // カテゴリー＋平均
        val analysisYoYTotalAverageData = getAnalysisYoYTotalAverageData(
            lastShoppingList,
            thisShoppingList,
            "average"
        )

        // カテゴリー＋年間
        val analysisYoYTotalYearData = getAnalysisYoYTotalData(
            lastShoppingList,
            thisShoppingList,
            null,
            "year"
        )

        val analysisYoYTotal = AnalysisYoYTotal(
            "Total",
            analysisYoYTotalMonthData,
            analysisYoYTotalAverageData,
            analysisYoYTotalYearData
        )

        return AnalysisYoYResult(
            ResponseCode.成功.code,
            AnalysisYoY(
                analysisYoYCategory,
                analysisYoYTotal
            )
        )

    }

    fun getAnalysisYoYCategoryData(
        lastShoppingList: List<Shopping>,
        thisShoppingList: List<Shopping>,
        category: Category,
        mm: MM? = null,
        timeFrame: String
    ): AnalysisYoYData {

        var filteringLastShoppingList: List<Shopping> = listOf()
        var filteringThisShoppingList: List<Shopping> = listOf()

        if (mm != null) {

            // 前年購入データをカテゴリー＋月で絞りこむ
            filteringLastShoppingList =
                lastShoppingList.filter { it.categoryId == category.id && it.shoppingDate.month.value == mm.value }
            // 今年購入データをカテゴリー＋月で絞りこむ
            filteringThisShoppingList =
                thisShoppingList.filter { it.categoryId == category.id && it.shoppingDate.month.value == mm.value }

        } else {
            // 前年購入データをカテゴリー＋月で絞りこむ
            filteringLastShoppingList =
                lastShoppingList.filter { it.categoryId == category.id }
            // 今年購入データをカテゴリー＋月で絞りこむ
            filteringThisShoppingList =
                thisShoppingList.filter { it.categoryId == category.id }
        }

        // 前年 - 金額
        val lastShoppingAmount =
            Amount(filteringLastShoppingList.sumOf { it.shoppingAmount.value })
        // 今年 - 金額
        val thisShoppingAmount =
            Amount(filteringThisShoppingList.sumOf { it.shoppingAmount.value })

        // 差分
        val differenceAmount = Amount(thisShoppingAmount.value - lastShoppingAmount.value)
        var yoyRatio = Ratio(0.0)
        if (lastShoppingAmount.value == 0 || thisShoppingAmount.value == 0) {
            yoyRatio = Ratio(0.0)
        } else {
            val yoyRatioString =
                String.format("%.2f", (thisShoppingAmount.value.toDouble() / lastShoppingAmount.value.toDouble() * 100))
            yoyRatio = Ratio(yoyRatioString.toDouble())
        }

        return AnalysisYoYData(
            timeFrame,
            lastShoppingAmount,
            thisShoppingAmount,
            differenceAmount,
            yoyRatio
        )
    }

    fun getAnalysisYoYCategoryAverageData(
        lastShoppingList: List<Shopping>,
        thisShoppingList: List<Shopping>,
        category: Category,
        timeFrame: String
    ): AnalysisYoYData {

        var filteringLastShoppingList: List<Shopping> = listOf()
        var filteringThisShoppingList: List<Shopping> = listOf()

        // 前年購入データをカテゴリー＋月で絞りこむ
        filteringLastShoppingList =
            lastShoppingList.filter { it.categoryId == category.id }

        // 今年購入データをカテゴリー＋月で絞りこむ
        filteringThisShoppingList =
            thisShoppingList.filter { it.categoryId == category.id }

        // 前年 - 金額
        val lastShoppingAverageAmount =
            Amount(filteringLastShoppingList.sumOf { it.shoppingAmount.value } / 12)
        // 今年 - 金額
        val thisShoppingAverageAmount =
            Amount(filteringThisShoppingList.sumOf { it.shoppingAmount.value } / 12)

        return AnalysisYoYData(
            timeFrame,
            lastShoppingAverageAmount,
            thisShoppingAverageAmount,
            Amount(0),
            Ratio(0.0)
        )
    }

    fun getAnalysisYoYTotalData(
        lastShoppingList: List<Shopping>,
        thisShoppingList: List<Shopping>,
        mm: MM? = null,
        timeFrame: String
    ): AnalysisYoYData {

        var filteringLastShoppingList: List<Shopping> = listOf()
        var filteringThisShoppingList: List<Shopping> = listOf()

        if (mm != null) {

            // 前年購入データをカテゴリー＋月で絞りこむ
            filteringLastShoppingList =
                lastShoppingList.filter { it.shoppingDate.month.value == mm.value }
            // 今年購入データをカテゴリー＋月で絞りこむ
            filteringThisShoppingList =
                thisShoppingList.filter { it.shoppingDate.month.value == mm.value }

        } else {
            // 前年購入データをカテゴリー＋月で絞りこむ
            filteringLastShoppingList = lastShoppingList
            // 今年購入データをカテゴリー＋月で絞りこむ
            filteringThisShoppingList = thisShoppingList
        }

        // 前年 - 金額
        val lastShoppingAmount =
            Amount(filteringLastShoppingList.sumOf { it.shoppingAmount.value })
        // 今年 - 金額
        val thisShoppingAmount =
            Amount(filteringThisShoppingList.sumOf { it.shoppingAmount.value })

        // 差分
        val differenceAmount = Amount(thisShoppingAmount.value - lastShoppingAmount.value)
        var yoyRatio = Ratio(0.0)
        if (lastShoppingAmount.value == 0 || thisShoppingAmount.value == 0) {
            yoyRatio = Ratio(0.0)
        } else {
            val yoyRatioString =
                String.format("%.2f", (thisShoppingAmount.value.toDouble() / lastShoppingAmount.value.toDouble() * 100))
            yoyRatio = Ratio(yoyRatioString.toDouble())
        }

        return AnalysisYoYData(
            timeFrame,
            lastShoppingAmount,
            thisShoppingAmount,
            differenceAmount,
            yoyRatio
        )
    }

    fun getAnalysisYoYTotalAverageData(
        lastShoppingList: List<Shopping>,
        thisShoppingList: List<Shopping>,
        timeFrame: String
    ): AnalysisYoYData {

        var filteringLastShoppingList: List<Shopping> = listOf()
        var filteringThisShoppingList: List<Shopping> = listOf()

        // 前年購入データをカテゴリー＋月で絞りこむ
        filteringLastShoppingList = lastShoppingList

        // 今年購入データをカテゴリー＋月で絞りこむ
        filteringThisShoppingList = thisShoppingList

        // 前年 - 金額
        val lastShoppingAverageAmount =
            Amount(filteringLastShoppingList.sumOf { it.shoppingAmount.value } / 12)
        // 今年 - 金額
        val thisShoppingAverageAmount =
            Amount(filteringThisShoppingList.sumOf { it.shoppingAmount.value } / 12)

        return AnalysisYoYData(
            timeFrame,
            lastShoppingAverageAmount,
            thisShoppingAverageAmount,
            Amount(0),
            Ratio(0.0)
        )
    }

}