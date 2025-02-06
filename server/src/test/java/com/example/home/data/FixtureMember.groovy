package com.example.home.data


import com.example.home.domain.entity.member.Member
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

    static メンバー_正常値() {
        return new Member(
                メンバーID_正常(),
                FixtureGroupList.所属グループID_正常(),
                メンバー番号_正常(),
                メンバー名_正常()
        )
    }
}