package com.example.home.datasource.template

import com.example.home.domain.entity.template.ShoppingEntryTemplate
import com.example.home.domain.repository.template.ShoppingEntryTemplateRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.domain.value_object.template.TemplateName
import com.example.home.domain.value_object.template.TemplateUseFlg
import com.example.home.domain.value_object.template.TmpId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsTmpShoppingEntry
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ShoppingEntryTemplateRepositoryImpl : ShoppingEntryTemplateRepository {

    override fun refer(groupsId: GroupsId?, templateId: TemplateId?): List<ShoppingEntryTemplate> {
        return transaction {
            TbTsTmpShoppingEntry.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    groupsId?.value?.let { condition = condition and (TbTsTmpShoppingEntry.groupsId eq it) }
                    templateId?.value?.let {
                        condition = condition and (TbTsTmpShoppingEntry.templateId eq it)
                    } // 仮の条件（適切に修正）
                    condition
                }
            }
                .orderBy(TbTsTmpShoppingEntry.id to SortOrder.ASC)
                .map {
                    ShoppingEntryTemplate(
                        TmpId(it[TbTsTmpShoppingEntry.id]),
                        GroupsId(it[TbTsTmpShoppingEntry.groupsId]),
                        TemplateId(it[TbTsTmpShoppingEntry.templateId]),
                        TemplateName(it[TbTsTmpShoppingEntry.tmpeName]),
                        MemberId(it[TbTsTmpShoppingEntry.tmpeMemberId]),
                        CategoryId(it[TbTsTmpShoppingEntry.tmpeCategoryId]),
                        ShoppingType(it[TbTsTmpShoppingEntry.tmpeType]),
                        ShoppingPayment(it[TbTsTmpShoppingEntry.tmpePayment]),
                        ShoppingSettlement(it[TbTsTmpShoppingEntry.tmpeSettlement]),
                        Amount(it[TbTsTmpShoppingEntry.tmpeAmount]),
                        ShoppingRemarks(it[TbTsTmpShoppingEntry.tmpeRemarks]),
                        TemplateUseFlg(it[TbTsTmpShoppingEntry.tmpeUseFlg])
                    )
                }
        }
    }

    override fun save(
        groupsId: GroupsId,
        templateId: TemplateId,
        templateName: TemplateName,
        memberId: MemberId,
        categoryId: CategoryId,
        shoppingType: ShoppingType,
        shoppingPayment: ShoppingPayment,
        shoppingSettlement: ShoppingSettlement,
        shoppingAmount: Amount,
        shoppingRemarks: ShoppingRemarks,
        templateUseFlg: TemplateUseFlg
    ): ShoppingEntryTemplate {
        return transaction {
            TbTsTmpShoppingEntry.insert {
                it[TbTsTmpShoppingEntry.groupsId] = groupsId.value
                it[TbTsTmpShoppingEntry.templateId] = templateId.value
                it[tmpeName] = templateName.value
                it[tmpeMemberId] = memberId.value
                it[tmpeCategoryId] = categoryId.value
                it[tmpeType] = shoppingType.value
                it[tmpePayment] = shoppingPayment.value
                it[tmpeSettlement] = shoppingSettlement.value
                it[tmpeAmount] = shoppingAmount.value
                it[tmpeRemarks] = shoppingRemarks.value
                it[tmpeUseFlg] = templateUseFlg.value
            }

            val shoppingInputTemplate = TbTsTmpShoppingEntry.select {
                (TbTsTmpShoppingEntry.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingEntry.templateId eq templateId.value) and
                        (TbTsTmpShoppingEntry.tmpeName eq templateName.value) and
                        (TbTsTmpShoppingEntry.tmpeMemberId eq memberId.value) and
                        (TbTsTmpShoppingEntry.tmpeCategoryId eq categoryId.value) and
                        (TbTsTmpShoppingEntry.tmpeType eq shoppingType.value) and
                        (TbTsTmpShoppingEntry.tmpePayment eq shoppingPayment.value) and
                        (TbTsTmpShoppingEntry.tmpeSettlement eq shoppingSettlement.value) and
                        (TbTsTmpShoppingEntry.tmpeAmount eq shoppingAmount.value) and
                        (TbTsTmpShoppingEntry.tmpeRemarks eq shoppingRemarks.value) and
                        (TbTsTmpShoppingEntry.tmpeUseFlg eq templateUseFlg.value)
            }.singleOrNull()

            return@transaction shoppingInputTemplate?.let {
                ShoppingEntryTemplate(
                    TmpId(it[TbTsTmpShoppingEntry.id]),
                    GroupsId(it[TbTsTmpShoppingEntry.groupsId]),
                    TemplateId(it[TbTsTmpShoppingEntry.templateId]),
                    TemplateName(it[TbTsTmpShoppingEntry.tmpeName]),
                    MemberId(it[TbTsTmpShoppingEntry.tmpeMemberId]),
                    CategoryId(it[TbTsTmpShoppingEntry.tmpeCategoryId]),
                    ShoppingType(it[TbTsTmpShoppingEntry.tmpeType]),
                    ShoppingPayment(it[TbTsTmpShoppingEntry.tmpePayment]),
                    ShoppingSettlement(it[TbTsTmpShoppingEntry.tmpeSettlement]),
                    Amount(it[TbTsTmpShoppingEntry.tmpeAmount]),
                    ShoppingRemarks(it[TbTsTmpShoppingEntry.tmpeRemarks]),
                    TemplateUseFlg(it[TbTsTmpShoppingEntry.tmpeUseFlg])
                )
            } ?: throw IllegalStateException("Failed to save the ShoppingEntryTemplate")
        }
    }

    override fun update(
        groupsId: GroupsId,
        templateId: TemplateId,
        templateName: TemplateName,
        memberId: MemberId,
        categoryId: CategoryId,
        shoppingType: ShoppingType,
        shoppingPayment: ShoppingPayment,
        shoppingSettlement: ShoppingSettlement,
        shoppingAmount: Amount,
        shoppingRemarks: ShoppingRemarks,
        templateUseFlg: TemplateUseFlg
    ): Int {
        return transaction {
            val updateRows = TbTsTmpShoppingEntry.update({
                (TbTsTmpShoppingEntry.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingEntry.templateId eq templateId.value)
            }) {
                it[tmpeName] = templateName.value
                it[tmpeMemberId] = memberId.value
                it[tmpeCategoryId] = categoryId.value
                it[tmpeType] = shoppingType.value
                it[tmpePayment] = shoppingPayment.value
                it[tmpeSettlement] = shoppingSettlement.value
                it[tmpeAmount] = shoppingAmount.value
                it[tmpeRemarks] = shoppingRemarks.value
                it[tmpeUseFlg] = templateUseFlg.value
            }
            return@transaction updateRows
        }
    }

    override fun delete(groupsId: GroupsId, templateId: TemplateId?): Int {
        return transaction {
            val deleteRows = TbTsTmpShoppingEntry.deleteWhere {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    groupsId.let { condition = condition and (TbTsTmpShoppingEntry.groupsId eq it.value) }
                    templateId?.let { condition = condition and (TbTsTmpShoppingEntry.templateId eq it.value) }
                    condition
                }
            }
            return@transaction deleteRows
        }
    }
}