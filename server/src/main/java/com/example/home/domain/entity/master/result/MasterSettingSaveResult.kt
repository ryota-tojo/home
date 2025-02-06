package com.example.home.domain.entity.master.result

import com.example.home.domain.entity.master.MasterSetting

data class MasterSettingSaveResult(
    val result: String,
    val masterSetting: MasterSetting? = null

)
