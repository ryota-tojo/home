package com.example.home.data.report

import com.example.home.data.category.FixtureCategory
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.member.FixtureMember
import com.example.home.domain.entity.report.*

class FixtureReport {

    static メンバー別未精算金額() {
        new MemberAndAmount(
                FixtureMember.メンバー_正常値(),
                FixtureEtc.金額_正常()
        )
    }

    static 精算レポート() {
        new SettlementReport(
                10,
                [メンバー別未精算金額()],
                FixtureEtc.金額_正常()
        )
    }

    static カテゴリー別残高金額() {
        new CategoryAndAmount(
                FixtureCategory.カテゴリー_正常値(),
                FixtureEtc.金額_正常()
        )
    }

    static 残高レポート() {
        new BalanceReport(
                [カテゴリー別残高金額()],
                FixtureEtc.金額_正常()
        )
    }

    static レポート_正常値() {
        new Report(
                FixtureEtc.年_正常(),
                FixtureEtc.月_正常(),
                精算レポート(),
                残高レポート()
        )
    }
}