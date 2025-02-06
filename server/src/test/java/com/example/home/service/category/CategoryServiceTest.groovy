package com.example.home.service.category

import com.example.home.data.category.FixtureCategory
import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.category.result.CategoryDeleteResult
import com.example.home.domain.entity.category.result.CategoryReferResult
import com.example.home.domain.entity.category.result.CategorySaveResult
import com.example.home.domain.entity.category.result.CategoryUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class CategoryServiceTest extends Specification {

    private CategoryRepository categoryRepository = Mock()
    private CategoryService sut = new CategoryService(categoryRepository)

    def "category_refer_#useCase"() {

        setup:

        when:
        def result = sut.refer(FixtureGroupList.所属グループID_正常())

        then:
        1 * categoryRepository.refer(FixtureGroupList.所属グループID_正常()) >> categoryList
        result == expected

        where:
        useCase               | expected                                                                               | categoryList
        "正常"                | new CategoryReferResult(ResponseCode.成功.code, [FixtureCategory.カテゴリー_正常値()]) | [FixtureCategory.カテゴリー_正常値()]
        "正常_カテゴリーなし" | new CategoryReferResult(sprintf(ResponseCode.成功_条件付き.code, ["CATEGORY_NOT_FOUND"]), null) | null
    }

    def "category_save_#useCase"() {

        setup:

        when:
        def result = sut.save(FixtureGroupList.所属グループID_正常(), FixtureCategory.カテゴリー番号_正常(), categoryName)

        then:
        saveCnt * categoryRepository.save(FixtureGroupList.所属グループID_正常(), FixtureCategory.カテゴリー番号_正常(), FixtureCategory.カテゴリー名_正常()) >> FixtureCategory.カテゴリー_正常値()
        result == expected

        where:
        useCase                     | expected                                                                            | saveCnt | categoryName
        "正常"                      | new CategorySaveResult(ResponseCode.成功.code, FixtureCategory.カテゴリー_正常値()) | 1       | FixtureCategory.カテゴリー名_正常()
        "異常_バリデーションエラー" | new CategorySaveResult(ResponseCode.バリデーションエラー.code, null)                | 0       | FixtureCategory.カテゴリー名_バリデーションエラー()
    }

    def "category_update_#useCase"() {

        setup:

        when:
        def result = sut.update(FixtureGroupList.所属グループID_正常(), FixtureCategory.カテゴリー番号_正常(), categoryName)

        then:
        updateCnt * categoryRepository.update(FixtureGroupList.所属グループID_正常(), FixtureCategory.カテゴリー番号_正常(), FixtureCategory.カテゴリー名_正常()) >> updateRows
        result == expected

        where:
        useCase                     | expected                                                            | updateCnt | categoryName                                        | updateRows
        "正常"                      | new CategoryUpdateResult(ResponseCode.成功.code, 1)                 | 1         | FixtureCategory.カテゴリー名_正常()                 | 1
        "異常_バリデーションエラー" | new CategoryUpdateResult(ResponseCode.バリデーションエラー.code, 0) | 0         | FixtureCategory.カテゴリー名_バリデーションエラー() | _
        "異常_データ不在エラー"     | new CategoryUpdateResult(ResponseCode.データ不在エラー.code, 0)     | 1         | FixtureCategory.カテゴリー名_正常()                 | 0
    }

    def "category_delete_#useCase"() {

        setup:

        when:
        def result = sut.delete(FixtureGroupList.所属グループID_正常(), categoryNo)

        then:
        1 * categoryRepository.delete(FixtureGroupList.所属グループID_正常(), categoryNo, null) >> deleteRows
        result == expected

        where:
        useCase                   | expected                                                        | categoryNo                            | deleteRows
        "正常_カテゴリー番号あり" | new CategoryDeleteResult(ResponseCode.成功.code, 1)             | FixtureCategory.カテゴリー番号_正常() | 1
        "正常_カテゴリー番号なし" | new CategoryDeleteResult(ResponseCode.成功.code, 1)             | null                                  | 1
        "異常_データ不在エラー"   | new CategoryDeleteResult(ResponseCode.データ不在エラー.code, 0) | FixtureCategory.カテゴリー番号_正常() | 0
    }
}
