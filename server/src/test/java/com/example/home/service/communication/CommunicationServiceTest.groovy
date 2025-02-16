package com.example.home.service.communication


import com.example.home.data.communication.FixtureCommunication
import com.example.home.data.etc.FixtureEtc
import com.example.home.data.group.FixtureGroupList
import com.example.home.domain.entity.communication.result.CommunicationDeleteResult
import com.example.home.domain.entity.communication.result.CommunicationReferResult
import com.example.home.domain.entity.communication.result.CommunicationSaveResult
import com.example.home.domain.entity.communication.result.CommunicationUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.communication.CommunicationRepository
import spock.lang.Specification

class CommunicationServiceTest extends Specification {

    private CommunicationRepository communicationRepository = Mock()
    private CommunicationService sut = new CommunicationService(communicationRepository)

    def "communication_refer_#useCase"() {

        setup:

        when:
        def result = sut.refer(giftId)

        then:
        1 * communicationRepository.refer(giftId) >> communicationList
        result == expected

        where:
        useCase           | expected                                                                                   | giftId                               | communicationList
        "正常"            | new CommunicationReferResult(ResponseCode.成功.code, [FixtureCommunication.ギフト_正常()]) | FixtureCommunication.ギフトID_正常() | [FixtureCommunication.ギフト_正常()]
        "正常_データなし" | new CommunicationReferResult(ResponseCode.成功.code, null)                                 | FixtureCommunication.ギフトID_正常() | null
        "正常_キーなし"   | new CommunicationReferResult(ResponseCode.成功.code, [FixtureCommunication.ギフト_正常()]) | null                                 | [FixtureCommunication.ギフト_正常()]
    }

    def "communication_save_#useCase"() {
        setup:
        def groupsId = FixtureGroupList.所属グループID_正常()
        def giftDate = FixtureCommunication.ギフト受取日_正常()
        def giftFrom = FixtureCommunication.ギフト贈り主_正常()
        def giftTo = FixtureCommunication.ギフト受取人_正常()
        def giftItem = FixtureCommunication.ギフト品目_正常()
        def giftAmount = FixtureEtc.金額_正常()
        def giftRemarks = FixtureCommunication.ギフト詳細_正常()
        def giftRtnDate = FixtureCommunication.返品日_正常()
        def giftRtnItem = FixtureCommunication.返品品目_正常()
        def giftRtnAmount = FixtureEtc.金額_正常()
        def giftRtnRemarks = FixtureCommunication.返品詳細_正常()
        def returnFlg = FixtureCommunication.返品フラグ_未返品()

        when:
        def result = sut.save(
                groupsId,
                giftDate,
                giftFrom,
                giftTo,
                giftItem,
                giftAmount,
                giftRemarks,
                giftRtnDate,
                giftRtnItem,
                giftRtnAmount,
                giftRtnRemarks,
                returnFlg
        )

        then:
        1 * communicationRepository.save(
                groupsId,
                giftDate,
                giftFrom,
                giftTo,
                giftItem,
                giftAmount,
                giftRemarks,
                giftRtnDate,
                giftRtnItem,
                giftRtnAmount,
                giftRtnRemarks,
                returnFlg
        ) >> communication
        result == expected

        where:
        useCase | expected                                                                                | giftId                               | communication
        "正常"  | new CommunicationSaveResult(ResponseCode.成功.code, FixtureCommunication.ギフト_正常()) | FixtureCommunication.ギフトID_正常() | FixtureCommunication.ギフト_正常()
    }

    def "communication_update_#useCase"() {
        setup:
        def giftId = FixtureCommunication.ギフトID_正常()
        def giftDate = FixtureCommunication.ギフト受取日_正常()
        def giftFrom = FixtureCommunication.ギフト贈り主_正常()
        def giftTo = FixtureCommunication.ギフト受取人_正常()
        def giftItem = FixtureCommunication.ギフト品目_正常()
        def giftAmount = FixtureEtc.金額_正常()
        def giftRemarks = FixtureCommunication.ギフト詳細_正常()
        def giftRtnDate = FixtureCommunication.返品日_正常()
        def giftRtnItem = FixtureCommunication.返品品目_正常()
        def giftRtnAmount = FixtureEtc.金額_正常()
        def giftRtnRemarks = FixtureCommunication.返品詳細_正常()
        def returnFlg = FixtureCommunication.返品フラグ_未返品()

        when:
        def result = sut.update(
                giftId,
                giftDate,
                giftFrom,
                giftTo,
                giftItem,
                giftAmount,
                giftRemarks,
                giftRtnDate,
                giftRtnItem,
                giftRtnAmount,
                giftRtnRemarks,
                returnFlg
        )

        then:
        1 * communicationRepository.update(
                giftId,
                giftDate,
                giftFrom,
                giftTo,
                giftItem,
                giftAmount,
                giftRemarks,
                giftRtnDate,
                giftRtnItem,
                giftRtnAmount,
                giftRtnRemarks,
                returnFlg
        ) >> updateRows
        result == expected

        where:
        useCase | expected                                                 | updateRows
        "正常"  | new CommunicationUpdateResult(ResponseCode.成功.code, 1) | 1
    }

    def "communication_delete_#useCase"() {
        setup:

        when:
        def result = sut.delete(groupsId, giftId)

        then:
        1 * communicationRepository.delete(groupsId, giftId) >> deleteRows
        result == expected

        where:
        useCase                        | expected                                                             | groupsId                               | giftId                               | deleteRows
        "正常_groupsIdあり_giftIdあり" | new CommunicationDeleteResult(ResponseCode.成功.code, 1)             | FixtureGroupList.所属グループID_正常() | FixtureCommunication.ギフトID_正常() | 1
        "正常_groupsIdなし_giftIdあり" | new CommunicationDeleteResult(ResponseCode.成功.code, 1)             | null                                   | FixtureCommunication.ギフトID_正常() | 1
        "正常_groupsIdあり_giftIdなし" | new CommunicationDeleteResult(ResponseCode.成功.code, 1)             | FixtureGroupList.所属グループID_正常() | null                                 | 1
        "正常_groupsIdなし_giftIdなし" | new CommunicationDeleteResult(ResponseCode.データ不在エラー.code, 0) | null                                   | null                                 | 0
    }

}
