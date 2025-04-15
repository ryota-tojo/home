package com.example.home.domain.repository.master

import com.example.home.domain.entity.master.MasterChoices
import com.example.home.domain.value_object.master.ChoicesItemNamePC
import com.example.home.domain.value_object.master.ChoicesItemNameSP
import com.example.home.domain.value_object.master.ChoicesItemNo
import com.example.home.domain.value_object.master.ChoicesItemType

interface ChoicesRepository {
    fun refer(): List<MasterChoices>? = null
    fun getItemName(itemType: ChoicesItemType, itemNo: ChoicesItemNo): MasterChoices? = null
    fun save(
        itemType: ChoicesItemType,
        itemNo: ChoicesItemNo,
        itemNamePC: ChoicesItemNamePC,
        itemNameSP: ChoicesItemNameSP
    ): MasterChoices? = null

    fun update(
        id: Int,
        itemType: ChoicesItemType,
        itemNo: ChoicesItemNo,
        itemNamePC: ChoicesItemNamePC,
        itemNameSP: ChoicesItemNameSP
    ): Int? = 0

    fun delete(id: Int): Int? = 0
}