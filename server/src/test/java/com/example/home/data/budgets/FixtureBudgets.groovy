package com.example.home.data.budgets

import com.example.home.data.category.FixtureCategory
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.fixed.FixtureFixed
import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.budgets.Budgets
import com.example.home.domain.value_object.etc.Amount

class FixtureBudgets {
    static 予算ID_正常() {
        1
    }

    static 予算金額_正常() {
        new Amount(10000)
    }

    static 予算_正常値() {
        return new Budgets(
                予算ID_正常(),
                FixtureGroupList.所属グループID_正常(),
                FixtureEtc.年_正常(),
                FixtureEtc.月_正常(),
                FixtureCategory.カテゴリーID_正常(),
                予算金額_正常(),
                FixtureFixed.確定フラグ_未確定()
        )
    }
}