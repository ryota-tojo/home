package com.example.home.service.fixed

import com.example.home.data.etc.FixtureEtc
import com.example.home.data.fixed.FixtureFixed
import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.fixed.result.FixedFixedResult
import com.example.home.domain.entity.fixed.result.FixedReferResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.repository.fixed.FixedRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import spock.lang.Specification

class FixedServiceTest extends Specification {
    private FixedRepository fixedRepository = Mock()
    private BudgetsRepository budgetsRepository = Mock()
    private ShoppingRepository shoppingRepository = Mock()
    private CommentRepository commentRepository = Mock()
    private FixedService sut = new FixedService(fixedRepository, budgetsRepository, shoppingRepository, commentRepository)

    def "fixed_refer_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def yyyy = FixtureEtc.年_正常()

        when:
        def result = sut.refer(groupsId, yyyy, null)

        then:
        1 * fixedRepository.refer(groupsId, yyyy, null) >> fixedList
        result == expected

        where:
        useCase             | expected                                                                           | fixedList
        "正常_12ヶ月データ" | new FixedReferResult(ResponseCode.成功.code, FixtureFixed.確定参照データ_12ヶ月()) | FixtureFixed.確定データ_12ヶ月()
        "正常_6ヶ月データ"  | new FixedReferResult(ResponseCode.成功.code, FixtureFixed.確定参照データ_6ヶ月())  | FixtureFixed.確定データ_6ヶ月()
        "正常_データなし"   | new FixedReferResult(ResponseCode.成功.code, FixtureFixed.確定参照データ_なし())   | FixtureFixed.確定データ_なし()
    }

    def "fixed_fixed_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def yyyy = FixtureEtc.年_正常()
        def mm = FixtureEtc.月_正常()

        when:
        def result = sut.fixed(groupsId, yyyy, mm)

        then:
        1 * budgetsRepository.fixed(groupsId, yyyy, mm) >> 1
        1 * shoppingRepository.fixed(groupsId, yyyy, mm) >> 1
        1 * commentRepository.fixed(groupsId, yyyy, mm) >> 1
        1 * fixedRepository.fixed(groupsId, yyyy, mm)
        result == expected

        where:
        useCase | expected
        "正常_" | new FixedFixedResult(ResponseCode.成功.code, 1, 1, 1)
    }

    def "fixed_unFixed_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def yyyy = FixtureEtc.年_正常()
        def mm = FixtureEtc.月_正常()

        when:
        def result = sut.unFixed(groupsId, yyyy, mm)

        then:
        1 * budgetsRepository.unFixed(groupsId, yyyy, mm) >> 1
        1 * shoppingRepository.unFixed(groupsId, yyyy, mm) >> 1
        1 * commentRepository.unFixed(groupsId, yyyy, mm) >> 1
        1 * fixedRepository.unFixed(groupsId, yyyy, mm)
        result == expected

        where:
        useCase | expected
        "正常_" | new FixedFixedResult(ResponseCode.成功.code, 1, 1, 1)
    }
}
