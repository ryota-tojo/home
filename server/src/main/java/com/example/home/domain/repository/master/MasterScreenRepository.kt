package com.example.home.domain.repository.master

import com.example.home.domain.entity.master.MasterScreen
import com.example.home.domain.value_object.master.ScreenId
import com.example.home.domain.value_object.master.ScreenName
import com.example.home.domain.value_object.master.ScreenRemarks

interface MasterScreenRepository {
    fun refer(screenId: ScreenId? = null): List<MasterScreen>
    fun save(screenId: ScreenId, screenName: ScreenName, screenRemarks: ScreenRemarks): MasterScreen
}
