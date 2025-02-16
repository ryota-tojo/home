package com.example.home.data.communication

import com.example.home.data.etc.FixtureEtc
import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.communication.Communication
import com.example.home.domain.value_object.communication.*

import java.time.LocalDate

class FixtureCommunication {
    static ギフトID_正常() {
        new GiftId(1)
    }

    static ギフト受取日_正常() {
        LocalDate.of(2025, 2, 2)

    }

    static ギフト贈り主_正常() {
        new GiftFrom("送り主")
    }

    static ギフト受取人_正常() {
        new GiftTo("受取人")
    }

    static ギフト品目_正常() {
        new GiftItem("ギフト品目")
    }

    static ギフト詳細_正常() {
        new GiftRemarks("ギフト詳細")
    }

    static 返品日_正常() {
        LocalDate.of(2025, 2, 2)
    }

    static 返品品目_正常() {
        new GiftItem("返品品目")
    }

    static 返品詳細_正常() {
        new GiftRemarks("返品詳細")
    }

    static 返品フラグ_未返品() {
        new GiftReturnFlg(0)
    }

    static ギフト_正常() {
        return new Communication(
                ギフトID_正常(),
                FixtureGroupList.所属グループID_正常(),
                ギフト受取日_正常(),
                ギフト贈り主_正常(),
                ギフト受取人_正常(),
                ギフト品目_正常(),
                FixtureEtc.金額_正常(),
                ギフト詳細_正常(),
                返品日_正常(),
                返品品目_正常(),
                FixtureEtc.金額_正常(),
                返品詳細_正常(),
                返品フラグ_未返品()
        )
    }
}

