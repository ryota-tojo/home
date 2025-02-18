package com.example.home.data.fixed


import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.fixed.Fixed
import com.example.home.domain.entity.fixed.FixedRefer
import com.example.home.domain.value_object.etc.FixedFlg
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.fixed.FixedId

class FixtureFixed {


    static 確定ID_正常() {
        new FixedId(1)
    }

    static 確定フラグ_未確定() {
        new FixedFlg(0)
    }

    static 確定フラグ_確定() {
        new FixedFlg(1)
    }

    static 確定データ_12ヶ月() {
        [
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(1)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(2)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(3)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(4)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(5)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(6)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(7)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(8)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(9)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(10)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(11)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(12)
                )
        ]
    }

    static 確定データ_6ヶ月() {
        [
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(1)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(2)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(3)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(4)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(5)
                ),
                new Fixed(
                        確定ID_正常(),
                        FixtureGroupList.所属グループID_正常(),
                        new YYYY(2025),
                        new MM(6)
                )
        ]
    }

    static 確定データ_なし() {
        []
    }

    static 確定参照データ_12ヶ月() {
        [
                new FixedRefer(
                        new YYYY(2025),
                        new MM(1),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(2),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(3),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(4),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(5),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(6),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(7),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(8),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(9),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(10),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(11),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(12),
                        true
                )
        ]
    }

    static 確定参照データ_6ヶ月() {
        [
                new FixedRefer(
                        new YYYY(2025),
                        new MM(1),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(2),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(3),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(4),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(5),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(6),
                        true
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(7),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(8),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(9),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(10),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(11),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(12),
                        false
                )
        ]
    }

    static 確定参照データ_なし() {
        [
                new FixedRefer(
                        new YYYY(2025),
                        new MM(1),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(2),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(3),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(4),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(5),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(6),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(7),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(8),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(9),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(10),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(11),
                        false
                ),
                new FixedRefer(
                        new YYYY(2025),
                        new MM(12),
                        false
                )
        ]
    }
}