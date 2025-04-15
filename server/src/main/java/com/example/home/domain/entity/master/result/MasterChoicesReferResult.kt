package com.example.home.domain.entity.master.result

import com.example.home.domain.entity.master.MasterChoices

data class MasterChoicesReferResult(
    val result: String,
    val masterChoices: List<MasterChoices>? = null

)
