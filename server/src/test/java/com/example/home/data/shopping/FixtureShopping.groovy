package com.example.home.data.shopping

import com.example.home.data.category.FixtureCategory
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.fixed.FixtureFixed
import com.example.home.data.group.FixtureGroupList
import com.example.home.data.member.FixtureMember
import com.example.home.data.user.FixtureUserInfo
import com.example.home.domain.entity.shopping.Shopping
import com.example.home.domain.value_object.shopping.*

import java.time.LocalDate

class FixtureShopping {
    static 購入ID_正常() {
        new ShoppingId(0)
    }

    static 購入日_正常() {
        LocalDate.of(2025, 2, 2)
    }

    static 購入種別_収入() {
        new ShoppingType(0)
    }

    static 購入種別_出費() {
        new ShoppingType(1)
    }

    static 購入種別_存在しない値() {
        new ShoppingType(999)
    }

    static 支払い方法_現金() {
        new ShoppingPayment(0)
    }

    static 支払い方法_カード() {
        new ShoppingPayment(1)
    }

    static 支払い方法_引落し() {
        new ShoppingPayment(2)
    }

    static 支払い方法_振込み() {
        new ShoppingPayment(3)
    }

    static 支払い方法_存在しない値() {
        new ShoppingPayment(999)
    }

    static 精算状態_未精算() {
        new ShoppingSettlement(0)
    }

    static 精算状態_精算済() {
        new ShoppingSettlement(1)
    }

    static 精算状態_存在しない値() {
        new ShoppingSettlement(999)
    }

    static 備考_正常() {
        new ShoppingRemarks("備考")
    }

    static 購入データ_正常() {
        return new Shopping(
                購入ID_正常(),
                FixtureGroupList.所属グループID_正常(),
                FixtureUserInfo.ユーザーID_正常(),
                購入日_正常(),
                FixtureMember.メンバーID_正常(),
                FixtureCategory.カテゴリーID_正常(),
                購入種別_出費(),
                支払い方法_現金(),
                精算状態_未精算(),
                FixtureEtc.金額_正常(),
                備考_正常(),
                FixtureFixed.確定フラグ_未確定()
        )
    }
}
