package com.example.home.domain.entity.master

import com.example.home.domain.value_object.master.ChoicesItemNamePC
import com.example.home.domain.value_object.master.ChoicesItemNameSP
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType

data class MasterChoices(
    val choicesItemType : ChoicesItemType,
    val choicesItemNo: ChoicesItemNo,
    val choicesItemNamePC: ChoicesItemNamePC,
    val choicesItemNameSP: ChoicesItemNameSP
)