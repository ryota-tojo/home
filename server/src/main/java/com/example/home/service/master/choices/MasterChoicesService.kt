package com.example.home.service.master.choices

import com.example.home.domain.entity.master.result.*
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.master.ChoicesRepository
import com.example.home.domain.value_object.master.ChoicesItemNamePC
import com.example.home.domain.value_object.master.ChoicesItemNameSP
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType
import org.springframework.stereotype.Service

@Service
class MasterChoicesService(
    private val choicesRepository: ChoicesRepository
) {
    fun refer(): MasterChoicesReferResult {
        val masterChoicesList = choicesRepository.refer()
        if (masterChoicesList.isNullOrEmpty()) {
            return MasterChoicesReferResult(
                ResponseCode.データ不在エラー.code,
                masterChoicesList
            )
        }
        return MasterChoicesReferResult(
            ResponseCode.成功.code,
            masterChoicesList
        )
    }

    fun getItemName(
        itemType: ChoicesItemType,
        itemNo: ChoicesItemNo
    ): MasterChoicesGetItemNameResult {

        val masterChoices = choicesRepository.getItemName(itemType, itemNo)
        if (masterChoices == null) {
            return MasterChoicesGetItemNameResult(
                ResponseCode.データ不在エラー.code,
                masterChoices
            )
        }
        return MasterChoicesGetItemNameResult(
            ResponseCode.成功.code,
            masterChoices
        )
    }

    fun save(
        itemType: ChoicesItemType,
        itemNo: ChoicesItemNo,
        itemNamePC: ChoicesItemNamePC,
        itemNameSP: ChoicesItemNameSP
    ): MasterChoicesSaveResult {
        val masterSetting = choicesRepository.save(itemType, itemNo, itemNamePC, itemNameSP)
        return MasterChoicesSaveResult(
            ResponseCode.成功.code,
            masterSetting
        )
    }

    fun update(
        id: Int,
        itemType: ChoicesItemType,
        itemNo: ChoicesItemNo,
        itemNamePC: ChoicesItemNamePC,
        itemNameSP: ChoicesItemNameSP
    ): MasterChoicesUpdateResult {
        val updateRows = choicesRepository.update(id, itemType, itemNo, itemNamePC, itemNameSP)
        if (updateRows == 0) {
            return MasterChoicesUpdateResult(
                ResponseCode.データ不在エラー.code,
                updateRows
            )
        }
        return MasterChoicesUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(id: Int): MasterChoicesDeleteResult {

        val deleteRows = choicesRepository.delete(id)
        if (deleteRows == 0) {
            return MasterChoicesDeleteResult(
                ResponseCode.データ不在エラー.code,
                0
            )
        }
        return MasterChoicesDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }

}