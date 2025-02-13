package com.example.home.service.master

import com.example.home.domain.entity.master.result.MasterSettingDeleteResult
import com.example.home.domain.entity.master.result.MasterSettingReferResult
import com.example.home.domain.entity.master.result.MasterSettingSaveResult
import com.example.home.domain.entity.master.result.MasterSettingUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.master.MasterSettingRepository
import com.example.home.domain.value_object.master.MasterSettingKey
import com.example.home.domain.value_object.master.MasterSettingValue
import com.example.home.util.ValidationCheck
import org.springframework.stereotype.Service

@Service
class MasterSettingService(
    private val masterSettingRepository: MasterSettingRepository
) {
    fun refer(masterSettingKey: MasterSettingKey? = null): MasterSettingReferResult {
        val masterSettingList = masterSettingRepository.refer(masterSettingKey)
        return MasterSettingReferResult(
            ResponseCode.成功.code,
            masterSettingList
        )
    }

    fun save(masterSettingKey: MasterSettingKey, masterSettingValue: MasterSettingValue): MasterSettingSaveResult {

        if (!ValidationCheck.symbol(masterSettingKey.toString()).result ||
            !ValidationCheck.symbol(masterSettingValue.toString()).result
        ) {
            return MasterSettingSaveResult(
                ResponseCode.バリデーションエラー.code,
                null
            )
        }

        val masterSettingList = masterSettingRepository.refer(masterSettingKey)
        if (masterSettingList != null) {
            return MasterSettingSaveResult(
                ResponseCode.重複エラー.code,
                null
            )
        }

        val masterSetting = masterSettingRepository.save(masterSettingKey, masterSettingValue)
        return MasterSettingSaveResult(
            ResponseCode.成功.code,
            masterSetting
        )
    }

    fun update(masterSettingKey: MasterSettingKey, masterSettingValue: MasterSettingValue): MasterSettingUpdateResult {

        if (!ValidationCheck.symbol(masterSettingKey.toString()).result ||
            !ValidationCheck.symbol(masterSettingValue.toString()).result
        ) {
            return MasterSettingUpdateResult(
                ResponseCode.バリデーションエラー.code,
                0
            )
        }

        val updateRows = masterSettingRepository.update(masterSettingKey, masterSettingValue)
        if (updateRows == 0) {
            return MasterSettingUpdateResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return MasterSettingUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(masterSettingKey: MasterSettingKey): MasterSettingDeleteResult {

        val deleteRows = masterSettingRepository.delete(masterSettingKey)
        if (deleteRows == 0) {
            return MasterSettingDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return MasterSettingDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }
}