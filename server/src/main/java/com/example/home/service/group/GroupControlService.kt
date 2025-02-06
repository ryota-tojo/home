package com.example.home.service.group

import com.example.home.datasource.group.GroupListRepositoryImpl
import com.example.home.datasource.group.GroupSettingRepositoryImpl
import com.example.home.domain.group.GroupListAndSetting
import com.example.home.domain.group.result.*
import com.example.home.domain.value_object.Constants
import com.example.home.domain.value_object.group.GroupName
import com.example.home.domain.value_object.group.GroupSettingKey
import com.example.home.domain.value_object.group.GroupSettingValue
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.infrastructure.persistence.repository.group.GroupListRepository
import com.example.home.infrastructure.persistence.repository.group.GroupSettingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class GroupControlService(
    private val groupListRepository: GroupListRepository = GroupListRepositoryImpl(),
    private val groupSettingRepository: GroupSettingRepository = GroupSettingRepositoryImpl()
) {
    fun refer(groupsId: GroupsId): GroupReferResult {
        val groupList = groupListRepository.refer(groupsId)
        val groupSetting = groupSettingRepository.refer(groupsId)
        return GroupReferResult(
            "SUCCESS",
            GroupListAndSetting(groupList, groupSetting)
        )
    }

    fun referAll(): GroupReferResult {
        val groupList = groupListRepository.referAll()
        val groupSetting = groupSettingRepository.referAll()
        return GroupReferResult(
            "SUCCESS",
            GroupListAndSetting(groupList, groupSetting)
        )
    }

    fun save(groupsId: GroupsId, groupName: GroupName): GroupSaveResult {
        if (Constants.INVALID_SYMBOL.any() { it in groupsId.toString() } ||
            Constants.INVALID_WORD.any() { it in groupsId.toString() }) {
            return GroupSaveResult("VALIDATION_ERROR")
        }
        if (Constants.INVALID_SYMBOL.any() { it in groupName.toString() } ||
            Constants.INVALID_WORD.any() { it in groupName.toString() }) {
            return GroupSaveResult("VALIDATION_ERROR")
        }
        if (groupListRepository.refer(groupsId).isNotEmpty()) {
            return GroupSaveResult("DUPLICATION_ERROR")
        }
        val groupList = groupListRepository.save(groupsId, groupName)
        val settings = listOf(
            Pair("display_yyyy", LocalDate.now().year.toString()),
            Pair("graph_type", "1"),
            Pair("slack_basic_webhook_url", ""),
            Pair("slack_basic_send_flg", "")
        )
        val groupSetting = settings.mapNotNull { (key, value) ->
            try {
                groupSettingRepository.save(groupsId, GroupSettingKey(key), GroupSettingValue(value))
            } catch (e: Exception) {
                null
            }
        }
        return GroupSaveResult("SUCCESS", groupList, groupSetting)
    }

    fun listUpdate(groupsId: GroupsId, groupName: GroupName): GroupListUpdateResult {
        if (Constants.INVALID_SYMBOL.any() { it in groupsId.value } ||
            Constants.INVALID_WORD.any() { it in groupsId.value }) {
            return GroupListUpdateResult("VALIDATION_ERROR")
        }
        if (Constants.INVALID_SYMBOL.any() { it in groupName.value } ||
            Constants.INVALID_WORD.any() { it in groupName.value }) {
            return GroupListUpdateResult("VALIDATION_ERROR")
        }
        if (groupListRepository.refer(groupsId).isEmpty()) {
            return GroupListUpdateResult("DATA_NOT_FOUND_ERROR")
        }
        val res = groupListRepository.update(groupsId, groupName)
        return GroupListUpdateResult("SUCCESS", res)
    }

    fun SettingUpdate(
        groupsId: GroupsId,
        settingKey: GroupSettingKey,
        settingValue: GroupSettingValue
    ): GroupSettingUpdateResult {
        if (Constants.INVALID_SYMBOL.any() { it in groupsId.value } ||
            Constants.INVALID_WORD.any() { it in groupsId.value }) {
            return GroupSettingUpdateResult("VALIDATION_ERROR")
        }
        if (Constants.INVALID_SYMBOL.any() { it in settingKey.value }) {
            return GroupSettingUpdateResult("VALIDATION_ERROR")
        }
        if (Constants.INVALID_SYMBOL.any() { it in settingValue.value }) {
            return GroupSettingUpdateResult("VALIDATION_ERROR")
        }
        if (groupSettingRepository.refer(groupsId).isEmpty()) {
            return GroupSettingUpdateResult("DATA_NOT_FOUND_ERROR")
        }
        val res = groupSettingRepository.update(groupsId, settingKey, settingValue)
        return GroupSettingUpdateResult("SUCCESS", res)
    }

    fun delete(groupsId: GroupsId): GroupDeleteResult {
        if (Constants.INVALID_SYMBOL.any() { it in groupsId.value } ||
            Constants.INVALID_WORD.any() { it in groupsId.value }) {
            return GroupDeleteResult("VALIDATION_ERROR")
        }
        if (groupListRepository.refer(groupsId).isEmpty()) {
            return GroupDeleteResult("DATA_NOT_FOUND_ERROR")
        }
        if (groupSettingRepository.refer(groupsId).isEmpty()) {
            return GroupDeleteResult("DATA_NOT_FOUND_ERROR")
        }
        val groupListDeletedResult = groupListRepository.delete(groupsId)
        val groupSettingDeletedResult = groupSettingRepository.delete(groupsId)

        return GroupDeleteResult("SUCCESS", groupListDeletedResult,groupSettingDeletedResult)
    }
}
