package com.example.home.domain.entity.group

data class GroupDeletionCounts(
    val groupListDelRowsInt: Int? = 0,
    val groupSettingDelRowsInt: Int? = 0,
    val groupInfoDelRowsInt: Int? = 0,
    val categoryDelRowsInt: Int? = 0,
    val memberDelRowsInt: Int? = 0,
    val shoppingInputTemplateDelRowsInt: Int? = 0,
    val shoppingSearchTemplateDelRowsInt: Int? = 0,
    val shoppingEntryTemplateDelRowsInt: Int? = 0,
    val budgetsDelRowsInt: Int? = 0,
    val shoppingDelRowsInt: Int? = 0,
    val fixedDelRowsInt: Int? = 0,
    val commentDelRowsInt: Int? = 0,
    val communicationDelRowsInt: Int? = 0
)

