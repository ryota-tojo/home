package com.example.home.service.shopping

import com.example.home.data.category.FixtureCategory
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.fixed.FixtureFixed
import com.example.home.data.group.FixtureGroupList
import com.example.home.data.master.FixtureChoices
import com.example.home.data.member.FixtureMember
import com.example.home.data.shopping.FixtureShopping
import com.example.home.domain.entity.master.MasterChoices
import com.example.home.domain.entity.shopping.result.ShoppingDeleteResult
import com.example.home.domain.entity.shopping.result.ShoppingReferResult
import com.example.home.domain.entity.shopping.result.ShoppingSaveResult
import com.example.home.domain.entity.shopping.result.ShoppingUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import spock.lang.Specification

class ShoppingServiceTest extends Specification {

    private ShoppingRepository shoppingRepository = Mock()
    private MemberRepository memberRepository = Mock()
    private CategoryRepository categoryRepository = Mock()
    private ChoicesRepository choicesRepository = Mock()
    private ShoppingService sut = new ShoppingService(shoppingRepository, memberRepository, categoryRepository, choicesRepository)

    def "shopping_refer_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()

        when:
        def result = sut.refer(groupsId, shoppingDateYYYY, shoppingDateMM, memberNo, categoryNo, type, payment, settlement, minAmount, maxAmount, remarks
        )

        then:
        1 * shoppingRepository.refer(groupsId, shoppingDateYYYY, shoppingDateMM, memberNo, categoryNo, type, payment, settlement, minAmount, maxAmount, remarks
        ) >> shoppingList
        result == expected

        where:
        useCase                                | expected                                                                         | shoppingList                    | shoppingDateYYYY     | shoppingDateMM       | memberNo                          | categoryNo                            | type                              | payment                           | settlement                        | minAmount              | maxAmount              | remarks
        "正常_All"                             | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureMember.メンバー番号_正常() | FixtureCategory.カテゴリー番号_正常() | FixtureShopping.買い物種別_出費() | FixtureShopping.支払い方法_現金() | FixtureShopping.精算状態_未精算() | FixtureEtc.金額_正常() | FixtureEtc.金額_正常() | FixtureShopping.備考_正常()
        "正常_年月"                            | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | FixtureEtc.年_正常() | FixtureEtc.月_正常() | null                              | null                                  | null                              | null                              | null                              | null                   | null                   | null
        "正常_年月 + メンバー番号"             | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureMember.メンバー番号_正常() | null                                  | null                              | null                              | null                              | null                   | null                   | null
        "正常_年月 + カテゴリー番号"           | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | FixtureEtc.年_正常() | FixtureEtc.月_正常() | null                              | FixtureCategory.カテゴリー番号_正常() | null                              | null                              | null                              | null                   | null                   | null
        "正常_メンバー番号 + 買い物種別"       | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | null                 | null                 | FixtureMember.メンバー番号_正常() | null                                  | FixtureShopping.買い物種別_出費() | null                              | null                              | null                   | null                   | null
        "正常_カテゴリー番号 + 支払い方法"     | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | null                 | null                 | null                              | FixtureCategory.カテゴリー番号_正常() | null                              | FixtureShopping.支払い方法_現金() | null                              | null                   | null                   | null
        "正常_年月 + 精算状態"                 | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | FixtureEtc.年_正常() | FixtureEtc.月_正常() | null                              | null                                  | null                              | null                              | FixtureShopping.精算状態_未精算() | null                   | null                   | null
        "正常_年月 + 最小金額"                 | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | FixtureEtc.年_正常() | FixtureEtc.月_正常() | null                              | null                                  | null                              | null                              | null                              | FixtureEtc.金額_正常() | null                   | null
        "正常_年月 + 最大金額"                 | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | FixtureEtc.年_正常() | FixtureEtc.月_正常() | null                              | null                                  | null                              | null                              | null                              | null                   | FixtureEtc.金額_正常() | null
        "正常_年月 + 最小金額 + 最大金額"      | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | FixtureEtc.年_正常() | FixtureEtc.月_正常() | null                              | null                                  | null                              | null                              | null                              | FixtureEtc.金額_正常() | FixtureEtc.金額_正常() | null
        "メンバー番号 + カテゴリー番号 + 備考" | new ShoppingReferResult(ResponseCode.成功.code, [FixtureShopping.買い物_正常()]) | [FixtureShopping.買い物_正常()] | null                 | null                 | FixtureMember.メンバー番号_正常() | FixtureCategory.カテゴリー番号_正常() | null                              | null                              | null                              | null                   | null                   | FixtureShopping.備考_正常()
        "正常_データなし"                      | new ShoppingReferResult(ResponseCode.成功.code, null)                            | null                            | FixtureEtc.年_正常() | FixtureEtc.月_正常() | FixtureMember.メンバー番号_正常() | FixtureCategory.カテゴリー番号_正常() | FixtureShopping.買い物種別_出費() | FixtureShopping.支払い方法_現金() | FixtureShopping.精算状態_未精算() | FixtureEtc.金額_正常() | FixtureEtc.金額_正常() | FixtureShopping.備考_正常()
    }

    def "shopping_save_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def shoppingDate = FixtureShopping.購入日_正常()
        def memberNo = FixtureMember.メンバー番号_正常()
        def categoryNo = FixtureCategory.カテゴリー番号_正常()
        def type = FixtureShopping.買い物種別_出費()
        def payment = FixtureShopping.支払い方法_現金()
        def settlement = FixtureShopping.精算状態_未精算()
        def amount = FixtureEtc.金額_正常()
        def remarks = FixtureShopping.備考_正常()

        when:
        def result = sut.save(groupsId, shoppingDate, memberNo, categoryNo, type, payment, settlement, amount, remarks
        )

        then:
        1 * memberRepository.refer(groupsId) >> memberList
        categoryCnt * categoryRepository.refer(groupsId) >> categoryList
        choiceCnt1 * choicesRepository.getItemName(FixtureChoices.選択肢_種別(), _) >> masterCoices1
        choiceCnt2 * choicesRepository.getItemName(FixtureChoices.選択肢_支払い方法(), _) >> masterCoices2
        choiceCnt3 * choicesRepository.getItemName(FixtureChoices.選択肢_精算状況(), _) >> masterCoices3
        saveCnt * shoppingRepository.save(groupsId, shoppingDate, memberNo, categoryNo, type, payment, settlement, amount, remarks
        ) >> shopping
        result == expected

        where:
        useCase               | expected                                                                      | shopping                      | memberList                        | categoryList                          | masterCoices1             | masterCoices2             | masterCoices3             | categoryCnt | choiceCnt1 | choiceCnt2 | choiceCnt3 | saveCnt
        "正常"                | new ShoppingSaveResult(ResponseCode.成功.code, FixtureShopping.買い物_正常()) | FixtureShopping.買い物_正常() | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | 1           | 1          | 1          | 1          | 1
        "異常_メンバーなし"   | new ShoppingSaveResult(ResponseCode.存在しないメンバー.code, null)            | null                          | []                                | null                                  | null                      | null                      | null                      | 0           | 0          | 0          | 0          | 0
        "異常_カテゴリーなし" | new ShoppingSaveResult(ResponseCode.存在しないカテゴリー.code, null)          | null                          | [FixtureMember.メンバー_正常値()] | []                                    | _                         | _                         | _                         | 1           | 0          | 0          | 0          | 0
        "異常_購入種別不正"   | new ShoppingSaveResult(ResponseCode.存在しない購入種別.code, null)            | null                          | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | null                      | _                         | _                         | 1           | 1          | 0          | 0          | 0
        "異常_支払い方法不正" | new ShoppingSaveResult(ResponseCode.存在しない支払い方法.code, null)          | null                          | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | null                      | _                         | 1           | 1          | 1          | 0          | 0
        "異常_精算状況不正"   | new ShoppingSaveResult(ResponseCode.存在しない精算状況.code, null)            | null                          | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | null                      | 1           | 1          | 1          | 1          | 0
    }

    def "shopping_isDuplication_#useCase"() {

        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def shoppingDate = FixtureShopping.購入日_正常()
        def memberNo = FixtureMember.メンバー番号_正常()
        def categoryNo = FixtureCategory.カテゴリー番号_正常()
        def type = FixtureShopping.買い物種別_出費()
        def payment = FixtureShopping.支払い方法_現金()
        def settlement = FixtureShopping.精算状態_未精算()
        def amount = FixtureEtc.金額_正常()
        def remarks = FixtureShopping.備考_正常()

        when:
        def result = sut.isDuplication(groupsId, shoppingDate, memberNo, categoryNo, type, payment, settlement, amount, remarks
        )

        then:
        1 * shoppingRepository.isDuplication(groupsId, shoppingDate, memberNo, categoryNo, type, payment, settlement, amount, remarks
        ) >> duplication
        result == expected

        where:
        useCase         | expected | duplication
        "正常_重複なし" | false    | false
        "正常_重複あり" | true     | true
    }

    def "shopping_update_#useCase"() {

        setup:
        def shoppingId = FixtureShopping.買い物ID_正常()
        def groupsId = FixtureGroupList.所属グループID_正常()
        def shoppingDate = FixtureShopping.購入日_正常()
        def memberNo = FixtureMember.メンバー番号_正常()
        def categoryNo = FixtureCategory.カテゴリー番号_正常()
        def type = FixtureShopping.買い物種別_出費()
        def payment = FixtureShopping.支払い方法_現金()
        def settlement = FixtureShopping.精算状態_未精算()
        def amount = FixtureEtc.金額_正常()
        def remarks = FixtureShopping.備考_正常()
        def fixedFlg = FixtureFixed.確定フラグ_未確定()

        when:
        def result = sut.update(shoppingId, groupsId, shoppingDate, memberNo, categoryNo, type, payment, settlement, amount, remarks, fixedFlg
        )

        then:
        1 * memberRepository.refer(groupsId) >> memberList
        categoryCnt * categoryRepository.refer(groupsId) >> categoryList
        choiceCnt1 * choicesRepository.getItemName(FixtureChoices.選択肢_種別(), _) >> masterCoices1
        choiceCnt2 * choicesRepository.getItemName(FixtureChoices.選択肢_支払い方法(), _) >> masterCoices2
        choiceCnt3 * choicesRepository.getItemName(FixtureChoices.選択肢_精算状況(), _) >> masterCoices3
        updateCnt * shoppingRepository.update(shoppingId, groupsId, shoppingDate, memberNo, categoryNo, type, payment, settlement, amount, remarks, fixedFlg
        ) >> updateRows
        result == expected

        where:
        useCase               | expected                                                            | updateRows | memberList                        | categoryList                          | masterCoices1             | masterCoices2             | masterCoices3             | categoryCnt | choiceCnt1 | choiceCnt2 | choiceCnt3 | updateCnt
        "正常"                | new ShoppingUpdateResult(ResponseCode.成功.code, 1)                 | 1          | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | 1           | 1          | 1          | 1          | 1
        "異常_メンバーなし"   | new ShoppingUpdateResult(ResponseCode.存在しないメンバー.code, 0)   | 0          | []                                | null                                  | null                      | null                      | null                      | 0           | 0          | 0          | 0          | 0
        "異常_カテゴリーなし" | new ShoppingUpdateResult(ResponseCode.存在しないカテゴリー.code, 0) | 0          | [FixtureMember.メンバー_正常値()] | []                                    | _                         | _                         | _                         | 1           | 0          | 0          | 0          | 0
        "異常_購入種別不正"   | new ShoppingUpdateResult(ResponseCode.存在しない購入種別.code, 0)   | 0          | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | null                      | _                         | _                         | 1           | 1          | 0          | 0          | 0
        "異常_支払い方法不正" | new ShoppingUpdateResult(ResponseCode.存在しない支払い方法.code, 0) | 0          | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | null                      | _                         | 1           | 1          | 1          | 0          | 0
        "異常_精算状況不正"   | new ShoppingUpdateResult(ResponseCode.存在しない精算状況.code, 0)   | 0          | [FixtureMember.メンバー_正常値()] | [FixtureCategory.カテゴリー_正常値()] | GroovyMock(MasterChoices) | GroovyMock(MasterChoices) | null                      | 1           | 1          | 1          | 1          | 0
    }

    def "shopping_delete_#useCase"() {

        setup:
        def shoppingId = FixtureShopping.買い物ID_正常()
        def groupsId = FixtureGroupList.所属グループID_正常()
        def shoppingDateYYYY = FixtureEtc.年_正常()
        def shoppingDateMM = FixtureEtc.月_正常()

        when:
        def result = sut.delete(shoppingId, groupsId, shoppingDateYYYY, shoppingDateMM)

        then:
        1 * shoppingRepository.delete(shoppingId, groupsId, shoppingDateYYYY, shoppingDateMM) >> deleteRows
        result == expected

        where:
        useCase           | expected                                                        | deleteRows
        "正常"            | new ShoppingDeleteResult(ResponseCode.成功.code, 1)             | 1
        "異常_データ不在" | new ShoppingDeleteResult(ResponseCode.データ不在エラー.code, 0) | 0
    }
}
