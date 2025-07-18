package com.example.home.domain.entity.master

import com.example.home.domain.value_object.master.ScreenId
import com.example.home.domain.value_object.master.ScreenName
import com.example.home.domain.value_object.master.ScreenRemarks

data class MasterScreen(
    val screenId: ScreenId,
    val screenName: ScreenName,
    val screenRemarks: ScreenRemarks
)