package com.example.home.service.master


import com.example.home.data.FixtureMasterSetting
import com.example.home.domain.entity.master.result.MasterSettingDeleteResult
import com.example.home.domain.entity.master.result.MasterSettingReferResult
import com.example.home.domain.entity.master.result.MasterSettingSaveResult
import com.example.home.domain.entity.master.result.MasterSettingUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.master.MasterSettingRepository
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class MasterSettingServiceTest extends Specification {
    private MasterSettingRepository masterSettingRepository = Mock()
    private MasterSettingService sut = new MasterSettingService(masterSettingRepository)

    def "refer_#useCase"() {

        setup:

        when:
        def result = sut.refer(FixtureMasterSetting.マスター設定キー_正常())

        then:
        1 * masterSettingRepository.refer(FixtureMasterSetting.マスター設定キー_正常()) >> masterSettingList
        result == expected

        where:
        useCase           | expected                                                                                                | masterSettingList
        "正常_データあり" | new MasterSettingReferResult(ResponseCode.成功.code, [FixtureMasterSetting.マスター設定_正常()])        | [FixtureMasterSetting.マスター設定_正常()]
        "正常_データなし" | new MasterSettingReferResult(sprintf(ResponseCode.成功_条件付き.code, ["SETTING_KEY_NOT_FOUND"]), null) | null
    }

    def "referAll_#useCase"() {

        setup:

        when:
        def result = sut.referAll()

        then:
        1 * masterSettingRepository.referAll() >> masterSettingList
        result == expected

        where:
        useCase           | expected                                                                                            | masterSettingList
        "正常_データあり" | new MasterSettingReferResult(ResponseCode.成功.code, [FixtureMasterSetting.マスター設定_正常()])    | [FixtureMasterSetting.マスター設定_正常()]
        "正常_データなし" | new MasterSettingReferResult(sprintf(ResponseCode.成功_条件付き.code, ["SETTING_NOT_FOUND"]), null) | null
    }

    def "save_#useCase"() {

        setup:

        when:
        def result = sut.save(masterSettingKey, masterSettingValue)

        then:
        referCnt * masterSettingRepository.refer(masterSettingKey) >> masterSettingList
        saveCnt * masterSettingRepository.save(masterSettingKey, masterSettingValue) >> masterSetting
        result == expected

        where:
        useCase                          | expected                                                                                      | masterSettingKey                                             | masterSettingValue                                         | masterSettingList                          | masterSetting                            | referCnt | saveCnt
        "正常"                           | new MasterSettingSaveResult(ResponseCode.成功.code, FixtureMasterSetting.マスター設定_正常()) | FixtureMasterSetting.マスター設定キー_正常()                 | FixtureMasterSetting.マスター設定値_正常()                 | null                                       | FixtureMasterSetting.マスター設定_正常() | 1        | 1
        "異常_バリデーションエラー_キー" | new MasterSettingSaveResult(ResponseCode.バリデーションエラー.code, null)                     | FixtureMasterSetting.マスター設定キー_バリデーションエラー() | FixtureMasterSetting.マスター設定値_正常()                 | _                                          | _                                        | 0        | 0
        "異常_バリデーションエラー_値"   | new MasterSettingSaveResult(ResponseCode.バリデーションエラー.code, null)                     | FixtureMasterSetting.マスター設定キー_正常()                 | FixtureMasterSetting.マスター設定値_バリデーションエラー() | _                                          | _                                        | 0        | 0
        "異常_重複エラー"                | new MasterSettingSaveResult(ResponseCode.重複エラー.code, null)                               | FixtureMasterSetting.マスター設定キー_正常()                 | FixtureMasterSetting.マスター設定値_正常()                 | [FixtureMasterSetting.マスター設定_正常()] | _                                        | 1        | 0
    }

    def "update_#useCase"() {

        setup:

        when:
        def result = sut.update(masterSettingKey, masterSettingValue)

        then:
        updateCnt * masterSettingRepository.update(masterSettingKey, masterSettingValue) >> updateRows
        result == expected

        where:
        useCase                          | expected                                                                 | masterSettingKey                                             | masterSettingValue                                         | updateRows | updateCnt
        "正常"                           | new MasterSettingUpdateResult(ResponseCode.成功.code, 1)                 | FixtureMasterSetting.マスター設定キー_正常()                 | FixtureMasterSetting.マスター設定値_正常()                 | 1          | 1
        "異常_バリデーションエラー_キー" | new MasterSettingUpdateResult(ResponseCode.バリデーションエラー.code, 0) | FixtureMasterSetting.マスター設定キー_バリデーションエラー() | FixtureMasterSetting.マスター設定値_正常()                 | 0          | 0
        "異常_バリデーションエラー_値"   | new MasterSettingUpdateResult(ResponseCode.バリデーションエラー.code, 0) | FixtureMasterSetting.マスター設定キー_正常()                 | FixtureMasterSetting.マスター設定値_バリデーションエラー() | 0          | 0
        "異常_データ不在エラー"          | new MasterSettingUpdateResult(ResponseCode.データ不在エラー.code, 0)     | FixtureMasterSetting.マスター設定キー_正常()                 | FixtureMasterSetting.マスター設定値_正常()                 | 0          | 1
    }

    def "delete_#useCase"() {

        setup:

        when:
        def result = sut.delete(FixtureMasterSetting.マスター設定キー_正常())

        then:
        1 * masterSettingRepository.delete(FixtureMasterSetting.マスター設定キー_正常()) >> deleteRows
        result == expected

        where:
        useCase                 | expected                                                             | deleteRows
        "正常"                  | new MasterSettingDeleteResult(ResponseCode.成功.code, 1)             | 1
        "異常_データ不在エラー" | new MasterSettingDeleteResult(ResponseCode.データ不在エラー.code, 0) | 0
    }
}
