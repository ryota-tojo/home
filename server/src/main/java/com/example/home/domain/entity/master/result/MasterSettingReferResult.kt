package com.example.home.domain.entity.master.result

import com.example.home.domain.entity.master.MasterSetting

data class MasterSettingReferResult(
    val result: String,
    val masterSettingList: List<MasterSetting>? = null

)
