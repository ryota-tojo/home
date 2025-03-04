package com.example.home.service.report


import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import spock.lang.Specification

class ReportServiceTest extends Specification {

    private GroupListRepository groupListRepository = Mock()
    private MemberRepository memberRepository = Mock()
    private CategoryRepository categoryRepository = Mock()
    private BudgetsRepository budgetsRepository = Mock()
    private ShoppingRepository shoppingRepository = Mock()

    private ReportService sut = new ReportService(groupListRepository, memberRepository, categoryRepository, budgetsRepository, shoppingRepository)

//    def "report_createReport_#useCase"() {
//        setup:
//        def groupsId = FixtureGroupList.所属グループID_正常()
//        def groupPassword = FixtureGroupList.所属グループパスワード_正常()
//        def yyyy = FixtureEtc.年_正常()
//        def mm = FixtureEtc.月_正常()
//
//        when:
//        def result = sut.createReport(groupsId, groupPassword, yyyy, mm)
//
//        then:
//        1 * groupListRepository.certification(groupsId, groupPassword) >> certificationResult
//        result == expected
//
//        where:
//        useCase                   | expected                                                                  | certificationResult | etcCnt
//        "正常"                    | new ReportResult(ResponseCode.成功.code, FixtureReport.レポート_正常値()) | true                | 1
//        "異常_グループ認証エラー" | new ReportResult(ResponseCode.グループ認証エラー.code, null)              | false               | 0
//    }
}
