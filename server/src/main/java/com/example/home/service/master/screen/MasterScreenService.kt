package com.example.home.service.master.screen

import com.example.home.domain.entity.master.result.MasterScreenReferResult
import com.example.home.domain.entity.master.result.MasterScreenSaveResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.master.MasterScreenRepository
import com.example.home.domain.value_object.master.ScreenId
import com.example.home.domain.value_object.master.ScreenName
import com.example.home.domain.value_object.master.ScreenRemarks
import org.springframework.stereotype.Service

@Service
class MasterScreenService(
    private val masterScreenRepository: MasterScreenRepository
) {
    fun refer(screenId: ScreenId? = null): MasterScreenReferResult {
        val masterScreenList = masterScreenRepository.refer(screenId = screenId)
        if (masterScreenList.isNullOrEmpty()) {
            return MasterScreenReferResult(
                ResponseCode.データ不在エラー.code,
                null
            )
        }
        return MasterScreenReferResult(
            ResponseCode.成功.code,
            masterScreenList
        )
    }

    fun save(screenId: ScreenId, screenName: ScreenName, screenRemarks: ScreenRemarks): MasterScreenSaveResult {
        val masterScreen = masterScreenRepository.save(screenId, screenName, screenRemarks)
        return MasterScreenSaveResult(
            ResponseCode.成功.code,
            masterScreen
        )
    }
}