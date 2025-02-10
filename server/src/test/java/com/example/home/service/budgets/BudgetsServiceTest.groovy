package com.example.home.service.budgets

import com.example.home.data.budgets.FixtureBudgets
import com.example.home.data.category.FixtureCategory
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.fixed.FixtureFixed
import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.budgets.result.BudgetsDeleteResult
import com.example.home.domain.entity.budgets.result.BudgetsReferResult
import com.example.home.domain.entity.budgets.result.BudgetsSaveResult
import com.example.home.domain.entity.budgets.result.BudgetsUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.budgets.BudgetsRepository
import spock.lang.Specification

class BudgetsServiceTest extends Specification {
    private BudgetsRepository budgetsRepository = Mock()
    private BudgetsService sut = new BudgetsService(budgetsRepository)

    def "budgets_refer_#useCase"() {

        setup:

        when:
        def result = sut.refer(groupsId, yyyy, mm, categoryNo)

        then:
        1 * budgetsRepository.refer(groupsId, yyyy, mm, categoryNo) >> budgetsList
        result == expected

        where:
        useCase                | expected                                                                       | groupsId                               | yyyy                 | mm                   | categoryNo                            | budgetsList
        "正常_全条件"          | new BudgetsReferResult(ResponseCode.成功.code, [FixtureBudgets.予算_正常値()]) | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureCategory.カテゴリー番号_正常() | [FixtureBudgets.予算_正常値()]
        "正常_グループIDのみ"  | new BudgetsReferResult(ResponseCode.成功.code, [FixtureBudgets.予算_正常値()]) | FixtureGroupList.所属グループID_正常() | null                 | null                 | null                                  | [FixtureBudgets.予算_正常値()]
        "正常_グループID＋年月" | new BudgetsReferResult(ResponseCode.成功.code, [FixtureBudgets.予算_正常値()]) | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | null                                  | [FixtureBudgets.予算_正常値()]
        "正常_データなし"      | new BudgetsReferResult(ResponseCode.成功.code, null)                           | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureCategory.カテゴリー番号_正常() | null
    }

    def "budgets_save_#useCase"() {

        setup:

        when:
        def result = sut.save(groupsId, yyyy, mm, categoryNo, amount)

        then:
        1 * budgetsRepository.refer(groupsId, yyyy, mm, categoryNo) >> budgetsList
        saveCnt * budgetsRepository.save(groupsId, yyyy, mm, categoryNo, amount) >> budgets
        result == expected

        where:
        useCase           | expected                                                                    | groupsId                               | yyyy                 | mm                   | categoryNo                            | amount                         | saveCnt | budgetsList                    | budgets
        "正常"            | new BudgetsSaveResult(ResponseCode.成功.code, FixtureBudgets.予算_正常値()) | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureCategory.カテゴリー番号_正常() | FixtureBudgets.予算金額_正常() | 1       | null                           | FixtureBudgets.予算_正常値()
        "異常_重複エラー" | new BudgetsSaveResult(ResponseCode.重複エラー.code, null)                   | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureCategory.カテゴリー番号_正常() | FixtureBudgets.予算金額_正常() | 0       | [FixtureBudgets.予算_正常値()] | _
    }

    def "budgets_update_#useCase"() {

        setup:

        when:
        def result = sut.update(groupsId, yyyy, mm, categoryNo, amount, fixedFlg)

        then:
        1 * budgetsRepository.update(groupsId, yyyy, mm, categoryNo, amount, fixedFlg) >> updateRows
        result == expected

        where:
        useCase                 | expected                                                       | groupsId                               | yyyy                 | mm                   | categoryNo                            | amount                         | fixedFlg                         | updateRows
        "正常"                  | new BudgetsUpdateResult(ResponseCode.成功.code, 1)             | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureCategory.カテゴリー番号_正常() | FixtureBudgets.予算金額_正常() | FixtureFixed.確定フラグ_未確定() | 1
        "異常_データ不在エラー" | new BudgetsUpdateResult(ResponseCode.データ不在エラー.code, 0) | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureCategory.カテゴリー番号_正常() | FixtureBudgets.予算金額_正常() | FixtureFixed.確定フラグ_確定()   | 0
    }

    def "budgets_delete_#useCase"() {

        setup:

        when:
        def result = sut.delete(groupsId, yyyy, mm, categoryNo)

        then:
        1 * budgetsRepository.delete(groupsId, yyyy, mm, categoryNo) >> deleteRows
        result == expected

        where:
        useCase                 | expected                                                       | groupsId                               | yyyy                 | mm                   | categoryNo                            | deleteRows
        "正常_全条件"           | new BudgetsDeleteResult(ResponseCode.成功.code, 1)             | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureCategory.カテゴリー番号_正常() | 1
        "正常_グループIDのみ"   | new BudgetsDeleteResult(ResponseCode.成功.code, 1)             | FixtureGroupList.所属グループID_正常() | null                 | null                 | null                                  | 1
        "正常_グループID＋年月"  | new BudgetsDeleteResult(ResponseCode.成功.code, 1)             | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | null                                  | 1
        "異常_データ不在エラー" | new BudgetsDeleteResult(ResponseCode.データ不在エラー.code, 0) | FixtureGroupList.所属グループID_正常() | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureCategory.カテゴリー番号_正常() | 0
    }
}
