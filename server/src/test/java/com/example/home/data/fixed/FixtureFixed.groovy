package com.example.home.data.fixed


import com.example.home.domain.value_object.etc.FixedFlg

class FixtureFixed {
    static 確定フラグ_未確定() {
        new FixedFlg(0)
    }

    static 確定フラグ_確定() {
        new FixedFlg(1)
    }
}