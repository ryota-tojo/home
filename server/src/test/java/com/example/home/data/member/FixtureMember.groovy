package com.example.home.data.member

import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.member.Member
import com.example.home.domain.value_object.member.MemberDeletedFlg
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo

class FixtureMember {
    static メンバーID_正常() {
        new MemberId(1)
    }

    static メンバー番号_正常() {
        new MemberNo(1)
    }

    static メンバー名_正常() {
        new MemberName("category_name")
    }

    static メンバー名_バリデーションエラー() {
        new MemberName("category_,name")
    }

    static 削除フラグ_未削除() {
        new MemberDeletedFlg(0)
    }

    static メンバー_正常値() {
        return new Member(
                メンバーID_正常(),
                FixtureGroupList.所属グループID_正常(),
                メンバー番号_正常(),
                メンバー名_正常(),
                削除フラグ_未削除()
        )
    }

    static メンバーID一覧_正常値() {
        return [
                メンバーID_正常()
        ]
    }
}