package com.example.home.domain.repository.master

import com.example.home.domain.entity.master.MasterChoices
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType

interface ChoicesRepository {
    fun refer(): List<MasterChoices>? = null
    fun getItemName(itemType: ChoicesItemType, itemNo: ChoicesItemNo): MasterChoices? = null
}