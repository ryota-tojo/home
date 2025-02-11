package com.example.home.data.etc

import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY

class FixtureEtc {
    static 年_正常() {
        new YYYY(2025)
    }

    static 月_正常() {
        new MM(1)
    }

    static 金額_正常() {
        new Amount(10000)
    }
}