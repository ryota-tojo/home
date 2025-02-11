package com.example.home.data.master

import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType

class FixtureChoices {

    static 選択肢_種別() {
        new ChoicesItemType("type")
    }

    static 選択肢番号_種別() {
        new ChoicesItemNo(1)
    }

    static 選択肢_支払い方法() {
        new ChoicesItemType("payment")
    }

    static 選択肢番号_支払い方法() {
        new ChoicesItemNo(2)
    }

    static 選択肢_精算状況() {
        new ChoicesItemType("settlement")
    }

    static 選択肢番号_精算状況() {
        new ChoicesItemNo(3)
    }
}