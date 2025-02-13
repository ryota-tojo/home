package com.example.home.service.group

import com.example.home.domain.entity.group.GroupListAndSetting
import com.example.home.domain.entity.group.result.*
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.group.GroupSettingRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.value_object.TsDefaultData
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.comment.Content
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.*
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.util.ValidationCheck
import org.springframework.stereotype.Service

@Service
class GroupControlService(
    private val groupListRepository: GroupListRepository,
    private val groupSettingRepository: GroupSettingRepository,
    private val groupInfoRepository: GroupInfoRepository,
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository,
    private val commentRepository: CommentRepository,
) {
    fun refer(groupsId: GroupsId): GroupReferResult {
        val groupList = groupListRepository.refer(groupsId)
        val groupSetting = groupSettingRepository.refer(groupsId)
        return GroupReferResult(
            ResponseCode.成功.code,
            GroupListAndSetting(groupList, groupSetting)
        )
    }

    fun save(groupsId: GroupsId, groupName: GroupName, groupPassword: GroupPassword): GroupSaveResult {
        if (!ValidationCheck.symbol(groupsId.toString()).result ||
            !ValidationCheck.symbol(groupName.toString()).result
        ) {
            return GroupSaveResult(ResponseCode.バリデーションエラー.code)
        }
        if (groupListRepository.refer(groupsId).isNotEmpty()) {
            return GroupSaveResult(ResponseCode.重複エラー.code)
        }
        val groupList = groupListRepository.save(groupsId, groupName, groupPassword)

        val settings = mutableListOf<Pair<String, String>>()
        for ((key, value) in TsDefaultData.GROUP_SETTING) {
            settings.add(Pair(key, value))
        }
        val groupSetting = settings.mapNotNull { (key, value) ->
            try {
                groupSettingRepository.save(groupsId, GroupSettingKey(key), GroupSettingValue(value))
            } catch (e: Exception) {
                null
            }
        }

        val categoryDefaultSettings = mutableListOf<Pair<String, String>>()
        for ((key, value) in TsDefaultData.CATEGORYS) {
            categoryDefaultSettings.add(Pair(key, value))
        }
        categoryDefaultSettings.mapNotNull { (no, value) ->
            try {
                categoryRepository.save(
                    groupsId,
                    CategoryId(no.toInt()),
                    CategoryName(value),
                )
            } catch (e: Exception) {
                null
            }
        }

        val memberDefaultSettings = mutableListOf<Pair<String, String>>()
        for ((key, value) in TsDefaultData.MEMBERS) {
            memberDefaultSettings.add(Pair(key, value))
        }
        memberDefaultSettings.mapNotNull { (no, value) ->
            try {
                memberRepository.save(
                    groupsId,
                    MemberNo(no.toInt()),
                    MemberName(value),
                )
            } catch (e: Exception) {
                null
            }
        }

        val commentDefaultSettings = mutableListOf<Pair<String, String>>()
        for ((yyyymm, content) in TsDefaultData.COMMENTS) {
            commentDefaultSettings.add(Pair(yyyymm, content))
        }
        commentDefaultSettings.mapNotNull { (yyyymm, content) ->
            try {
                commentRepository.save(
                    groupsId,
                    YYYY(yyyymm.take(4).toInt()),
                    MM(yyyymm.takeLast(2).toInt()),
                    Content(content)
                )
            } catch (e: Exception) {
                null
            }
        }
        return GroupSaveResult(
            ResponseCode.成功.code,
            groupList,
            groupSetting
        )
    }

    fun listUpdate(
        groupsId: GroupsId,
        groupName: GroupName?,
        groupPassword: GroupPassword?,
    ): GroupListUpdateResult {
        if (!ValidationCheck.symbol(groupsId.toString()).result ||
            !ValidationCheck.symbol(groupName.toString()).result
        ) {
            return GroupListUpdateResult(ResponseCode.バリデーションエラー.code)
        }
        if (groupListRepository.refer(groupsId).isEmpty()) {
            return GroupListUpdateResult(ResponseCode.データ不在エラー.code)
        }
        val res = groupListRepository.update(groupsId, groupName, groupPassword)
        return GroupListUpdateResult(ResponseCode.成功.code, res)
    }

    fun settingUpdate(
        groupsId: GroupsId,
        settingKey: GroupSettingKey,
        settingValue: GroupSettingValue
    ): GroupSettingUpdateResult {
        if (!ValidationCheck.symbol(groupsId.toString()).result ||
            !ValidationCheck.symbol(settingKey.toString()).result ||
            !ValidationCheck.symbol(settingValue.toString()).result
        ) {
            return GroupSettingUpdateResult(ResponseCode.バリデーションエラー.code)
        }
        if (groupSettingRepository.refer(groupsId).isEmpty()) {
            return GroupSettingUpdateResult(ResponseCode.データ不在エラー.code)
        }
        val res = groupSettingRepository.update(groupsId, settingKey, settingValue)
        return GroupSettingUpdateResult(ResponseCode.成功.code, res)
    }

    fun delete(groupsId: GroupsId): GroupDeleteResult {
        if (!ValidationCheck.symbol(groupsId.toString()).result) {
            return GroupDeleteResult(ResponseCode.バリデーションエラー.code)
        }
        if (groupListRepository.refer(groupsId).isEmpty()) {
            return GroupDeleteResult(ResponseCode.データ不在エラー.code)
        }
        if (groupSettingRepository.refer(groupsId).isEmpty()) {
            return GroupDeleteResult(ResponseCode.データ不在エラー.code)
        }
        val groupListDeletedResult = groupListRepository.delete(groupsId)
        val groupSettingDeletedResult = groupSettingRepository.delete(groupsId)
        groupInfoRepository.delete(groupsId)
        categoryRepository.delete(groupsId)
        memberRepository.delete(groupsId)
        commentRepository.delete(groupsId)

        return GroupDeleteResult(
            ResponseCode.成功.code,
            groupListDeletedResult,
            groupSettingDeletedResult
        )
    }
}
