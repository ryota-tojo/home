package com.example.home.data.comment

import com.example.home.data.etc.FixtureEtc
import com.example.home.data.fixed.FixtureFixed
import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.comment.Comment
import com.example.home.domain.value_object.comment.Content

class FixtureComment {
    static コメントID_正常() {
        1
    }

    static コンテンツ_正常() {
        new Content("コンテンツ")
    }

    static コメント_正常() {
        return new Comment(
                コメントID_正常(),
                FixtureGroupList.所属グループID_正常(),
                FixtureEtc.年_正常(),
                FixtureEtc.月_正常(),
                コンテンツ_正常(),
                FixtureFixed.確定フラグ_未確定()
        )
    }
}