package com.example.home.service.template

import com.example.home.data.category.FixtureCategory
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.group.FixtureGroupList
import com.example.home.data.master.FixtureChoices
import com.example.home.data.member.FixtureMember
import com.example.home.data.shopping.FixtureShopping
import com.example.home.data.template.FixtureShoppingEntryTemplate
import com.example.home.domain.entity.master.MasterChoices
import com.example.home.domain.entity.template.result.ShoppingEntryTemplateDeleteResult
import com.example.home.domain.entity.template.result.ShoppingEntryTemplateReferResult
import com.example.home.domain.entity.template.result.ShoppingEntryTemplateSaveResult
import com.example.home.domain.entity.template.result.ShoppingEntryTemplateUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.template.ShoppingEntryTemplateRepository
import spock.lang.Specification

class ShoppingEntryTemplateServiceTest extends Specification {

    private ShoppingEntryTemplateRepository shoppingEntryTemplateRepository = Mock()
    private MemberRepository memberRepository = Mock()
    private CategoryRepository categoryRepository = Mock()
    private ChoicesRepository choicesRepository = Mock()
    private ShoppingEntryTemplateService sut = new ShoppingEntryTemplateService(shoppingEntryTemplateRepository, memberRepository, categoryRepository, choicesRepository)

    def "shoppingEntryTemplate_refer_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def templateId = FixtureShoppingEntryTemplate.テンプレートID_正常()

        when:
        def result = sut.refer(groupsId, templateId)

        then:
        1 * shoppingEntryTemplateRepository.refer(groupsId, templateId) >> templateList
        result == expected

        where:
        useCase           | expected                                                                                                                       | templateList
        "正常"            | new ShoppingEntryTemplateReferResult(ResponseCode.成功.code, [FixtureShoppingEntryTemplate.購入データ入力テンプレート_正常()]) | [FixtureShoppingEntryTemplate.購入データ入力テンプレート_正常()]
        "正常_データなし" | new ShoppingEntryTemplateReferResult(ResponseCode.成功.code, null)                                                             | null
    }

    def "shoppingEntryTemplate_save_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def templateName = FixtureShoppingEntryTemplate.テンプレート名_正常()
        def memberId = FixtureMember.メンバーID_正常()
        def categoryId = FixtureCategory.カテゴリーID_正常()
        def type = FixtureShopping.購入種別_出費()
        def payment = FixtureShopping.支払い方法_現金()
        def settlement = FixtureShopping.精算状態_未精算()
        def amount = FixtureEtc.金額_正常()
        def remarks = FixtureShopping.備考_正常()
        def UseFlag = FixtureShoppingEntryTemplate.使用フラグ_使用()

        when:
        def result = sut.save(groupsId, templateId, templateName, memberId, categoryId, type, payment, settlement, amount, remarks, UseFlag)

        then:
        referCnt * shoppingEntryTemplateRepository.refer(groupsId, templateId) >> tmpList
        memberCnt * memberRepository.refer(groupsId) >> memberList
        categoryCnt * categoryRepository.refer(groupsId) >> categoryList
        choiceCnt1 * choicesRepository.getItemName(FixtureChoices.選択肢_種別(), _) >> masterCoices1
        choiceCnt2 * choicesRepository.getItemName(FixtureChoices.選択肢_支払い方法(), _) >> masterCoices2
        choiceCnt3 * choicesRepository.getItemName(FixtureChoices.選択肢_精算状況(), _) >> masterCoices3
        saveCnt * shoppingEntryTemplateRepository.save(groupsId, templateId, templateName, memberId, categoryId, type, payment, settlement, amount, remarks, UseFlag) >> template
        result == expected

        where:
        useCase                     | expected                                                                                                                    | templateId                                                         | tmpList                                                          | memberList                        | categoryList                          | masterCoices1             | masterCoices2             | masterCoices3             | template                                                       | referCnt | memberCnt | categoryCnt | choiceCnt1 | choiceCnt2 | choiceCnt3 | saveCnt
        "正常"                      | new ShoppingEntryTemplateSaveResult(ResponseCode.成功.code, FixtureShoppingEntryTemplate.購入データ入力テンプレート_正常()) | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | []                                                               | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | FixtureShoppingEntryTemplate.購入データ入力テンプレート_正常() | 1        | 1         | 1           | 1          | 1          | 1          | 1
        "異常_バリエーションエラー" | new ShoppingEntryTemplateSaveResult(ResponseCode.バリデーションエラー.code, null)                                           | FixtureShoppingEntryTemplate.テンプレートID_バリエーションエラー() | _                                                                | _                                 | _                                     | _                         | _                         | _                         | _                                                              | 0        | 0         | 0           | 0          | 0          | 0          | 0
        "異常_重複エラー"           | new ShoppingEntryTemplateSaveResult(ResponseCode.重複するテンプレートID.code, null)                                         | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | [FixtureShoppingEntryTemplate.購入データ入力テンプレート_正常()] | _                                 | _                                     | _                         | _                         | _                         | _                                                              | 1        | 0         | 0           | 0          | 0          | 0          | 0
        "異常_メンバーなし"         | new ShoppingEntryTemplateSaveResult(ResponseCode.存在しないメンバー.code, null)                                             | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | []                                                               | []                                | [FixtureCategory.カテゴリー_正常値()] | _                         | _                         | _                         | _                                                              | 1        | 1         | 0           | 0          | 0          | 0          | 0
        "異常_カテゴリーなし"       | new ShoppingEntryTemplateSaveResult(ResponseCode.存在しないカテゴリー.code, null)                                           | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | []                                                               | [FixtureMember.メンバー_正常値()] | []                                    | _                         | _                         | _                         | _                                                              | 1        | 1         | 1           | 0          | 0          | 0          | 0
        "異常_購入種別不正"         | new ShoppingEntryTemplateSaveResult(ResponseCode.存在しない購入種別.code, null)                                             | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | []                                                               | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | null                      | _                         | _                         | _                                                              | 1        | 1         | 1           | 1          | 0          | 0          | 0
        "異常_支払い方法不正"       | new ShoppingEntryTemplateSaveResult(ResponseCode.存在しない支払い方法.code, null)                                           | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | []                                                               | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | null                      | _                         | _                                                              | 1        | 1         | 1           | 1          | 1          | 0          | 0
        "異常_精算状況不正"         | new ShoppingEntryTemplateSaveResult(ResponseCode.存在しない精算状況.code, null)                                             | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | []                                                               | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | null                      | _                                                              | 1        | 1         | 1           | 1          | 1          | 1          | 0
    }

    def "shoppingEntryTemplate_update_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def templateName = FixtureShoppingEntryTemplate.テンプレート名_正常()
        def memberId = FixtureMember.メンバーID_正常()
        def categoryId = FixtureCategory.カテゴリーID_正常()
        def type = FixtureShopping.購入種別_出費()
        def payment = FixtureShopping.支払い方法_現金()
        def settlement = FixtureShopping.精算状態_未精算()
        def amount = FixtureEtc.金額_正常()
        def remarks = FixtureShopping.備考_正常()
        def UseFlag = FixtureShoppingEntryTemplate.使用フラグ_使用()

        when:
        def result = sut.update(groupsId, templateId, templateName, memberId, categoryId, type, payment, settlement, amount, remarks, UseFlag)

        then:
        memberCnt * memberRepository.refer(groupsId) >> memberList
        categoryCnt * categoryRepository.refer(groupsId) >> categoryList
        choiceCnt1 * choicesRepository.getItemName(FixtureChoices.選択肢_種別(), _) >> masterCoices1
        choiceCnt2 * choicesRepository.getItemName(FixtureChoices.選択肢_支払い方法(), _) >> masterCoices2
        choiceCnt3 * choicesRepository.getItemName(FixtureChoices.選択肢_精算状況(), _) >> masterCoices3
        updateCnt * shoppingEntryTemplateRepository.update(groupsId, templateId, templateName, memberId, categoryId, type, payment, settlement, amount, remarks, UseFlag) >> updateRows
        result == expected

        where:
        useCase                     | expected                                                                         | templateId                                                         | memberList                        | categoryList                          | masterCoices1             | masterCoices2             | masterCoices3             | updateRows | memberCnt | categoryCnt | choiceCnt1 | choiceCnt2 | choiceCnt3 | updateCnt
        "正常"                      | new ShoppingEntryTemplateUpdateResult(ResponseCode.成功.code, 1)                 | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | 1          | 1         | 1           | 1          | 1          | 1          | 1
        "異常_バリエーションエラー" | new ShoppingEntryTemplateUpdateResult(ResponseCode.バリデーションエラー.code, 0) | FixtureShoppingEntryTemplate.テンプレートID_バリエーションエラー() | _                                 | _                                     | _                         | _                         | _                         | 0          | 0         | 0           | 0          | 0          | 0          | 0
        "異常_メンバーなし"         | new ShoppingEntryTemplateUpdateResult(ResponseCode.存在しないメンバー.code, 0)   | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | []                                | [FixtureCategory.カテゴリー_正常値()] | _                         | _                         | _                         | 0          | 1         | 0           | 0          | 0          | 0          | 0
        "異常_カテゴリーなし"       | new ShoppingEntryTemplateUpdateResult(ResponseCode.存在しないカテゴリー.code, 0) | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | [FixtureMember.メンバー_正常値()] | []                                    | _                         | _                         | _                         | 0          | 1         | 1           | 0          | 0          | 0          | 0
        "異常_購入種別不正"         | new ShoppingEntryTemplateUpdateResult(ResponseCode.存在しない購入種別.code, 0)   | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | null                      | _                         | _                         | 0          | 1         | 1           | 1          | 0          | 0          | 0
        "異常_支払い方法不正"       | new ShoppingEntryTemplateUpdateResult(ResponseCode.存在しない支払い方法.code, 0) | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | null                      | _                         | 0          | 1         | 1           | 1          | 1          | 0          | 0
        "異常_精算状況不正"         | new ShoppingEntryTemplateUpdateResult(ResponseCode.存在しない精算状況.code, 0)   | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | null                      | 0          | 1         | 1           | 1          | 1          | 1          | 0
        "異常_データ不在エラー"     | new ShoppingEntryTemplateUpdateResult(ResponseCode.データ不在エラー.code, 0)     | FixtureShoppingEntryTemplate.テンプレートID_正常()                 | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | 0          | 1         | 1           | 1          | 1          | 1          | 1
    }

    def "shoppingEntryTemplate_delete_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def templateId = FixtureShoppingEntryTemplate.テンプレートID_正常()

        when:
        def result = sut.delete(groupsId, templateId)

        then:
        1 * shoppingEntryTemplateRepository.delete(groupsId, templateId) >> deleteRows
        result == expected

        where:
        useCase                 | expected                                                                     | deleteRows
        "正常"                  | new ShoppingEntryTemplateDeleteResult(ResponseCode.成功.code, 1)             | 1
        "異常_データ不在エラー" | new ShoppingEntryTemplateDeleteResult(ResponseCode.データ不在エラー.code, 0) | 0
    }
}







