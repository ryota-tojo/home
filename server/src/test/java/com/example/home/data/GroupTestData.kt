package com.example.home.data

import com.example.home.domain.entity.group.GroupList
import com.example.home.domain.entity.group.GroupSetting
import com.example.home.domain.value_object.group.*
import java.time.LocalDate

object GroupTestData {
    fun グループsID_正常値(): GroupsId {
        return GroupsId("groups_id")
    }

    fun グループsID_バリデーションエラー値_SYMBOL(): GroupsId {
        return GroupsId("groups_@id")
    }

    fun グループsID_バリデーションエラー値_WORD(): GroupsId {
        return GroupsId("groups_adminid")
    }

    fun グループ名_正常値(): GroupName {
        return GroupName("groups_name")
    }

    fun グループ名_バリデーションエラー値_SYMBOL(): GroupName {
        return GroupName("groups_@name")
    }

    fun グループ名_バリデーションエラー値_WORD(): GroupName {
        return GroupName("groups_adminname")
    }

    fun グループパスワード_正常値(): GroupPassword {
        return GroupPassword("groups_password")
    }

    fun グループ設定キー_正常値(): GroupSettingKey {
        return GroupSettingKey("group_setting_key")
    }

    fun グループ設定キー_バリデーションエラー値_SYMBOL(): GroupSettingKey {
        return GroupSettingKey("group_@setting_key")
    }

    fun グループ設定キー_バリデーションエラー値_WORD(): GroupSettingKey {
        return GroupSettingKey("group_adminsetting_key")
    }

    fun グループ設定値_正常値(): GroupSettingValue {
        return GroupSettingValue("group_setting_value")
    }

    fun グループ設定値_バリデーションエラー値_SYMBOL(): GroupSettingValue {
        return GroupSettingValue("group_@setting_value")
    }

    fun グループ設定値_バリデーションエラー値_WORD(): GroupSettingValue {
        return GroupSettingValue("group_adminsetting_value")
    }


    fun グループ一覧_正常値(): GroupList {
        return GroupList(
            1,
            グループsID_正常値(),
            グループ名_正常値(),
            グループパスワード_正常値()
        )
    }

    fun グループ設定_正常値(): GroupSetting {
        return GroupSetting(
            1,
            グループsID_正常値(),
            グループ設定キー_正常値(),
            グループ設定値_正常値()
        )
    }

    fun 所属グループ一覧リスト_正常値(): List<GroupList> {
        return listOf(
            グループ一覧_正常値()
        )
    }

    fun 所属グループ設定リスト_正常値(): List<GroupSetting> {
        val settings = listOf(
            Pair("display_yyyy", LocalDate.now().year.toString()),
            Pair("graph_type", "1"),
            Pair("slack_basic_webhook_url", ""),
            Pair("slack_basic_send_flg", "")
        )
        val groupSetting = settings.mapIndexed { index, (key, value) ->
            GroupSetting(
                id = index + 1,
                groupsId = GroupsId("groups_id"),
                settingKey = GroupSettingKey(key),
                settingValue = GroupSettingValue(value)
            )
        }
        return groupSetting
    }
}
