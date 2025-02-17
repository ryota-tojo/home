package com.example.home.data.category

import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.category.Category
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo

class FixtureCategory {
    static カテゴリーID_正常() {
        new CategoryId(1)
    }

    static カテゴリー番号_正常() {
        new CategoryNo(1)
    }

    static カテゴリー名_正常() {
        new CategoryName("category_name")
    }

    static カテゴリー名_バリデーションエラー() {
        new CategoryName("category_,name")
    }

    static カテゴリー_正常値() {
        return new Category(
                カテゴリーID_正常(),
                FixtureGroupList.所属グループID_正常(),
                カテゴリー番号_正常(),
                カテゴリー名_正常()
        )
    }

    static カテゴリーID一覧_正常値() {
        return [
                カテゴリーID_正常()
        ]
    }
}