package com.example.home.data.group

import com.example.home.domain.entity.group.GroupDeletionCounts

class FixtureGroupDelete {

    static 所属グループ削除() {
        return new GroupDeletionCounts(
                1, // groupListDelRowsInt
                1, // groupSettingDelRowsInt
                0, // groupInfoDelRowsInt
                0, // categoryDelRowsInt
                0, // memberDelRowsInt
                0, // shoppingInputTemplateDelRowsInt
                0, // shoppingSearchTemplateDelRowsInt
                0, // shoppingEntryTemplateDelRowsInt
                0, // budgetsDelRowsInt
                0, // shoppingDelRowsInt
                0, // fixedDelRowsInt
                0, // commentDelRowsInt
                0  // communicationDelRowsInt
        );
    }
}