package com.example.home.service.group


import com.example.home.data.group.FixtureGroupList
import com.example.home.data.group.FixtureGroupListAndSetting
import com.example.home.data.group.FixtureGroupSetting
import com.example.home.domain.entity.group.result.GroupDeleteResult
import com.example.home.domain.entity.group.result.GroupListUpdateResult
import com.example.home.domain.entity.group.result.GroupReferResult
import com.example.home.domain.entity.group.result.GroupSaveResult
import com.example.home.domain.entity.group.result.GroupSettingUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.group.GroupSettingRepository
import com.example.home.domain.repository.member.MemberRepository
import spock.lang.Specification

class GroupControlServiceTest extends Specification {

    private GroupListRepository groupListRepository = Mock()
    private GroupSettingRepository groupSettingRepository = Mock()
    private GroupInfoRepository groupInfoRepository = Mock()
    private MemberRepository memberRepository = Mock()
    private CategoryRepository categoryRepository = Mock()
    private CommentRepository commentRepository = Mock()
    private GroupControlService sut = new GroupControlService(
            groupListRepository,
            groupSettingRepository,
            groupInfoRepository,
            memberRepository,
            categoryRepository,
            commentRepository
    )

    def "groupList_refer_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()

        when:
        def result = sut.refer(groupsId)

        then:
        1 * groupListRepository.refer(_) >> groupList
        1 * groupSettingRepository.refer(_) >> groupSetting
        result == expected

        where:
        useCase           | expected                                                                                               | groupList                                  | groupSetting
        "正常"            | new GroupReferResult(ResponseCode.成功.code, FixtureGroupListAndSetting.所属グループ一覧と設定_正常()) | [FixtureGroupList.所属グループ一覧_正常()] | FixtureGroupSetting.所属グループ設定_正常()
        "正常_データなし" | new GroupReferResult(sprintf(ResponseCode.成功_条件付き.code, ["GROUP_NOT_FOUND"]), null)              | null                                       | null
    }

    def "groupList_save_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def groupPassword = FixtureGroupList.所属グループパスワード_正常()
        when:
        def result = sut.save(groupsId, groupName, groupPassword)

        then:
        glrCnt * groupListRepository.refer(groupsId) >> groupList
        glsCnt * groupListRepository.save(_, _, _) >> FixtureGroupList.所属グループ一覧_正常()
        etcCnt * groupSettingRepository.save(_, _, _)
        etcCnt * categoryRepository.save(_, _, _)
        etcCnt * memberRepository.save(_, _, _)
        etcCnt * commentRepository.save(_, _, _, _)

        result == expected

        where:
        useCase                     | expected                                                                                  | groupName                                              | groupList                                  | glrCnt | glsCnt | etcCnt
        "正常"                      | new GroupSaveResult(ResponseCode.成功.code, FixtureGroupList.所属グループ一覧_正常(), []) | FixtureGroupList.所属グループ名_正常()                 | []                                         | 1      | 1      | _
        "異常_バリデーションエラー" | new GroupSaveResult(ResponseCode.バリデーションエラー.code, null, null)                   | FixtureGroupList.所属グループ名_バリデーションエラー() | []                                         | 0      | 0      | 0
        "異常_重複エラー"           | new GroupSaveResult(ResponseCode.重複エラー.code, null, null)                             | FixtureGroupList.所属グループ名_正常()                 | [FixtureGroupList.所属グループ一覧_正常()] | 1      | 0      | 0
    }

    def "groupList_listUpdate_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def groupPassword = FixtureGroupList.所属グループパスワード_正常()
        when:
        def result = sut.listUpdate(groupsId, groupName, groupPassword)

        then:
        updateCnt * groupListRepository.update(_, _, _) >> updateRows
        result == expected

        where:
        useCase                     | expected                                                             | groupName                                              | updateRows | updateCnt
        "正常"                      | new GroupListUpdateResult(ResponseCode.成功.code, 1)                 | FixtureGroupList.所属グループ名_正常()                 | 1          | 1
        "異常_バリデーションエラー" | new GroupListUpdateResult(ResponseCode.バリデーションエラー.code, 0) | FixtureGroupList.所属グループ名_バリデーションエラー() | 0          | 0
        "異常_データ不在エラー"     | new GroupListUpdateResult(ResponseCode.データ不在エラー.code, 0)     | FixtureGroupList.所属グループ名_正常()                 | 0          | 1
    }

    def "groupList_settingUpdate_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def settingValue = FixtureGroupSetting.所属グループ設定値_正常()

        when:
        def result = sut.settingUpdate(groupsId, settingKey, settingValue)

        then:
        updateCnt * groupSettingRepository.update(_, _, _) >> updateRows
        result == expected

        where:
        useCase                     | expected                                                                | settingKey                                                      | updateRows | updateCnt
        "正常"                      | new GroupSettingUpdateResult(ResponseCode.成功.code, 1)                 | FixtureGroupSetting.所属グループ設定キー_正常()                 | 1          | 1
        "異常_バリデーションエラー" | new GroupSettingUpdateResult(ResponseCode.バリデーションエラー.code, 0) | FixtureGroupSetting.所属グループ設定キー_バリデーションエラー() | 0          | 0
        "異常_データ不在エラー"     | new GroupSettingUpdateResult(ResponseCode.データ不在エラー.code, 0)     | FixtureGroupSetting.所属グループ設定キー_正常()                 | 0          | 1
    }

    def "groupList_delete_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()

        when:
        def result = sut.delete(groupsId)

        then:
        glrCnt * groupListRepository.refer(groupsId) >> groupList
        gsrCnt * groupSettingRepository.refer(groupsId) >> GroupSettingList
        gsdCnt * groupSettingRepository.delete(groupsId) >> gsDeleteRows
        gldCnt * groupListRepository.delete(groupsId) >> glDeleteRows
        _ * groupInfoRepository.delete(groupsId)
        _ * categoryRepository.delete(groupsId)
        _ * memberRepository.delete(groupsId)
        _ * commentRepository.delete(groupsId)
        result == expected

        where:
        useCase                             | expected                                                        | groupList                                  | GroupSettingList                            | gsDeleteRows | glDeleteRows | glrCnt | gsrCnt | gsdCnt | gldCnt
        "正常"                              | new GroupDeleteResult(ResponseCode.成功.code, 1, 1)             | [FixtureGroupList.所属グループ一覧_正常()] | FixtureGroupSetting.所属グループ設定_正常() | 1            | 1            | 1      | 1      | 1      | 1
        "異常_グループ一覧データ不在エラー" | new GroupDeleteResult(ResponseCode.データ不在エラー.code, 0, 0) | []                                         | _                                           | 0            | 0            | 1      | 0      | 0      | 0
        "異常_グループ設定データ不在エラー" | new GroupDeleteResult(ResponseCode.データ不在エラー.code, 0, 0) | [FixtureGroupList.所属グループ一覧_正常()] | []                                          | 0            | 0            | 1      | 1      | 0      | 0
    }
}
