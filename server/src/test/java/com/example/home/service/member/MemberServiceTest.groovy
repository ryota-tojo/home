package com.example.home.service.member


import com.example.home.data.FixtureGroupList
import com.example.home.data.FixtureMember
import com.example.home.domain.entity.member.result.MemberDeleteResult
import com.example.home.domain.entity.member.result.MemberReferResult
import com.example.home.domain.entity.member.result.MemberSaveResult
import com.example.home.domain.entity.member.result.MemberUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.member.MemberRepository
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class MemberServiceTest extends Specification {

    private MemberRepository memberRepository = Mock()
    private MemberService sut = new MemberService(memberRepository)

    def "refer_#useCase"() {

        setup:

        when:
        def result = sut.refer(FixtureGroupList.所属グループID_正常())

        then:
        1 * memberRepository.refer(FixtureGroupList.所属グループID_正常()) >> categoryList
        result == expected

        where:
        useCase             | expected                                                                         | categoryList
        "正常"              | new MemberReferResult(ResponseCode.成功.code, [FixtureMember.メンバー_正常値()]) | [FixtureMember.メンバー_正常値()]
        "正常_メンバーなし" | new MemberReferResult(ResponseCode.成功_条件付き.code, null)                     | null
    }

    def "save_#useCase"() {

        setup:

        when:
        def result = sut.save(FixtureGroupList.所属グループID_正常(), FixtureMember.メンバー番号_正常(), memberName)

        then:
        saveCnt * memberRepository.save(FixtureGroupList.所属グループID_正常(), FixtureMember.メンバー番号_正常(), FixtureMember.メンバー名_正常()) >> FixtureMember.メンバー_正常値()
        result == expected

        where:
        useCase                     | expected                                                                      | saveCnt | memberName
        "正常"                      | new MemberSaveResult(ResponseCode.成功.code, FixtureMember.メンバー_正常値()) | 1       | FixtureMember.メンバー名_正常()
        "異常_バリデーションエラー" | new MemberSaveResult(ResponseCode.バリデーションエラー.code, null)            | 0       | FixtureMember.メンバー名_バリデーションエラー()
    }

    def "update_#useCase"() {

        setup:

        when:
        def result = sut.update(FixtureGroupList.所属グループID_正常(), FixtureMember.メンバー番号_正常(), memberName)

        then:
        updateCnt * memberRepository.update(FixtureGroupList.所属グループID_正常(), FixtureMember.メンバー番号_正常(), FixtureMember.メンバー名_正常()) >> updateRows
        result == expected

        where:
        useCase                     | expected                                                          | updateCnt | memberName                                      | updateRows
        "正常"                      | new MemberUpdateResult(ResponseCode.成功.code, 1)                 | 1         | FixtureMember.メンバー名_正常()                 | 1
        "異常_バリデーションエラー" | new MemberUpdateResult(ResponseCode.バリデーションエラー.code, 0) | 0         | FixtureMember.メンバー名_バリデーションエラー() | _
        "異常_データ不在エラー"     | new MemberUpdateResult(ResponseCode.データ不在エラー.code, 0)     | 1         | FixtureMember.メンバー名_正常()                 | 0
    }

    def "delete_#useCase"() {

        setup:

        when:
        def result = sut.delete(FixtureGroupList.所属グループID_正常(), memberNo)

        then:
        1 * memberRepository.delete(FixtureGroupList.所属グループID_正常(), memberNo, null) >> deleteRows
        result == expected

        where:
        useCase                 | expected                                                      | memberNo                          | deleteRows
        "正常_メンバー番号あり" | new MemberDeleteResult(ResponseCode.成功.code, 1)             | FixtureMember.メンバー番号_正常() | 1
        "正常_メンバー番号なし" | new MemberDeleteResult(ResponseCode.成功.code, 1)             | null                              | 1
        "異常_データ不在エラー" | new MemberDeleteResult(ResponseCode.データ不在エラー.code, 0) | FixtureMember.メンバー番号_正常() | 0
    }
}
