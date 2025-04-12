package com.example.home.service.group

import com.example.home.domain.entity.group.GroupDeletionCounts
import com.example.home.domain.entity.group.GroupListAndSetting
import com.example.home.domain.entity.group.result.*
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.budgets.BudgetsRepository
import com.example.home.domain.repository.category.CategoryRepository
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.repository.communication.CommunicationRepository
import com.example.home.domain.repository.fixed.FixedRepository
import com.example.home.domain.repository.group.GroupInfoRepository
import com.example.home.domain.repository.group.GroupListRepository
import com.example.home.domain.repository.group.GroupSettingRepository
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.repository.shopping.ShoppingRepository
import com.example.home.domain.repository.template.ShoppingEntryTemplateRepository
import com.example.home.domain.repository.template.ShoppingInputTemplateRepository
import com.example.home.domain.repository.template.ShoppingSearchTemplateRepository
import com.example.home.domain.value_object.TsDefaultData
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
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
    private val shoppingInputTemplateRepository: ShoppingInputTemplateRepository,
    private val shoppingSearchTemplateRepository: ShoppingSearchTemplateRepository,
    private val shoppingEntryTemplateRepository: ShoppingEntryTemplateRepository,
    private val budgetsRepository: BudgetsRepository,
    private val shoppingRepository: ShoppingRepository,
    private val fixedRepository: FixedRepository,
    private val commentRepository: CommentRepository,
    private val communicationRepository: CommunicationRepository,
) {
    fun refer(
        groupsId: GroupsId? = null,
        offset: Long? = null,
        limit: Int? = null,
    ): GroupReferResult {
        val groupList = groupListRepository.refer(groupsId)

        if (groupList.isNullOrEmpty()) {
            return GroupReferResult(
                ResponseCode.データ不在エラー.code,
                null
            )
        }

        val groupListAndSetting = groupList.map { group ->
            GroupListAndSetting(
                group,
                groupSettingRepository.refer(group.groupsId)
            )
        }

        return GroupReferResult(
            ResponseCode.成功.code,
            groupListAndSetting
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
        for ((key, value) in TsDefaultData.CATEGORIES) {
            categoryDefaultSettings.add(Pair(key, value))
        }
        println("-------------")
        categoryDefaultSettings.mapNotNull { (key, value) ->
            try {
                categoryRepository.save(
                    groupsId,
                    CategoryNo(key.toInt()),
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
        memberDefaultSettings.mapNotNull { (key, value) ->
            try {
                memberRepository.save(
                    groupsId,
                    MemberNo(key.toInt()),
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
        groupName: GroupName? = null,
        groupPassword: GroupPassword? = null,
    ): GroupListUpdateResult {
        if (!ValidationCheck.symbol(groupsId.toString()).result ||
            !ValidationCheck.symbol(groupName.toString()).result
        ) {
            return GroupListUpdateResult(ResponseCode.バリデーションエラー.code)
        }
        val updateRows = groupListRepository.update(groupsId, groupName, groupPassword)
        if (updateRows == 0) {
            return GroupListUpdateResult(ResponseCode.データ不在エラー.code)
        }
        return GroupListUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
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
        val updateRows = groupSettingRepository.update(groupsId, settingKey, settingValue)
        if (updateRows == 0) {
            return GroupSettingUpdateResult(ResponseCode.データ不在エラー.code)
        }
        return GroupSettingUpdateResult(ResponseCode.成功.code, updateRows)
    }

    fun delete(groupsId: GroupsId): GroupDeleteResult {
        if (groupListRepository.refer(groupsId).isEmpty()) {
            return GroupDeleteResult(ResponseCode.データ不在エラー.code)
        }
        if (groupSettingRepository.refer(groupsId).isEmpty()) {
            return GroupDeleteResult(ResponseCode.データ不在エラー.code)
        }
        val groupListDelRows = groupListRepository.delete(groupsId)
        val groupSettingDelRows = groupSettingRepository.delete(groupsId)
        val groupInfoDelRows = groupInfoRepository.delete(groupsId)
        val categoryDelRows = categoryRepository.delete(groupsId)
        val memberDelRows = memberRepository.delete(groupsId)
        val shoppingInputTemplateDelRows = shoppingInputTemplateRepository.delete(groupsId)
        val shoppingSearchTemplateDelRows = shoppingSearchTemplateRepository.delete(groupsId)
        val shoppingEntryTemplateDelRows = shoppingEntryTemplateRepository.delete(groupsId)
        val budgetsDelRows = budgetsRepository.delete(groupsId)
        val shoppingDelRows = shoppingRepository.delete(groupsId = groupsId)
        val fixedDelRows = fixedRepository.delete(groupsId)
        val commentDelRows = commentRepository.delete(groupsId)
        val communicationDelRows = communicationRepository.delete(groupsId)

        val groupDeletionCounts = GroupDeletionCounts(
            groupListDelRows,
            groupSettingDelRows,
            groupInfoDelRows,
            categoryDelRows,
            memberDelRows,
            shoppingInputTemplateDelRows,
            shoppingSearchTemplateDelRows,
            shoppingEntryTemplateDelRows,
            budgetsDelRows,
            shoppingDelRows,
            fixedDelRows,
            commentDelRows,
            communicationDelRows
        )

        return GroupDeleteResult(
            ResponseCode.成功.code,
            groupDeletionCounts
        )
    }
}
