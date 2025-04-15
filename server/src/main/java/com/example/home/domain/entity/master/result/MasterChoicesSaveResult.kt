package com.example.home.domain.entity.master.result

import com.example.home.domain.entity.master.MasterChoices

data class MasterChoicesSaveResult(
    val result: String,
    val masterChoices: MasterChoices? = null

)
