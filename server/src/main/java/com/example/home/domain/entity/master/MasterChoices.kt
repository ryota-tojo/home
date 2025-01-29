package com.example.home.domain.entity.master

import com.example.home.domain.value_object.master.ChoicesItemType
import com.example.home.domain.value_object.master.ChoicesItemName
import com.example.home.domain.value_object.master.ChoicesItemNo

data class MasterChoices(
    val choicesItemType : ChoicesItemType,
    val choicesItemNo: ChoicesItemNo,
    val choicesItemName: ChoicesItemName
) {
}