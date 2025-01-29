package com.example.home.domain.entity.group.result

import com.example.home.domain.entity.group.GroupListAndSetting
import com.example.home.domain.entity.group.GroupListAndSettingList

data class GroupReferAllResult(
    val result: String,
    val groupListAndSettingListList: List<GroupListAndSettingList>
)
