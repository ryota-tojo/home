package com.example.home.domain.entity.master.result

import com.example.home.domain.entity.master.MasterScreen

data class MasterScreenReferResult(
    val result: String,
    val masterScreenList: List<MasterScreen>? = null

)
