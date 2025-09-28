package com.example.home.datasource.template

import com.example.home.domain.entity.template.ShoppingInputTemplate
import com.example.home.domain.repository.template.ShoppingInputTemplateRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.etc.Amount
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.shopping.ShoppingPayment
import com.example.home.domain.value_object.shopping.ShoppingRemarks
import com.example.home.domain.value_object.shopping.ShoppingSettlement
import com.example.home.domain.value_object.shopping.ShoppingType
import com.example.home.domain.value_object.template.*
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsTmpShoppingEntry
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsTmpShoppingInput
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsTmpShoppingSearch
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ShoppingInputTemplateRepositoryImpl : ShoppingInputTemplateRepository {

    override fun refer(groupsId: GroupsId?, templateId: TemplateId?): List<ShoppingInputTemplate> {
        return transaction {
            TbTsTmpShoppingInput.select {
                Op.build {
                    var condition: Op<Boolean>? = null
                    groupsId?.value?.let {
                        condition = (condition?.and(TbTsTmpShoppingInput.groupsId eq it)) ?: (TbTsTmpShoppingInput.groupsId eq it)
                    }
                    templateId?.value?.let {
                        condition = (condition?.and(TbTsTmpShoppingInput.templateId eq it)) ?: (TbTsTmpShoppingInput.templateId eq it)
                    }
                    condition ?: Op.TRUE
                }
            }
                .orderBy(
                    TbTsTmpShoppingInput.deletedFlg to SortOrder.ASC,
                    TbTsTmpShoppingInput.templateNo to SortOrder.ASC,
                    TbTsTmpShoppingInput.tmpiUseFlg to SortOrder.DESC
                )
                .map {
                    ShoppingInputTemplate(
                        TmpId(it[TbTsTmpShoppingInput.id]),
                        GroupsId(it[TbTsTmpShoppingInput.groupsId]),
                        TemplateNo(it[TbTsTmpShoppingInput.templateNo]),
                        TemplateId(it[TbTsTmpShoppingInput.templateId]),
                        TemplateName(it[TbTsTmpShoppingInput.tmpiName]),
                        MemberId(it[TbTsTmpShoppingInput.tmpiMemberId]),
                        CategoryId(it[TbTsTmpShoppingInput.tmpiCategoryId]),
                        ShoppingType(it[TbTsTmpShoppingInput.tmpiType]),
                        ShoppingPayment(it[TbTsTmpShoppingInput.tmpiPayment]),
                        ShoppingSettlement(it[TbTsTmpShoppingInput.tmpiSettlement]),
                        Amount(it[TbTsTmpShoppingInput.tmpiAmount]),
                        ShoppingRemarks(it[TbTsTmpShoppingInput.tmpiRemarks]),
                        TemplateUseFlg(it[TbTsTmpShoppingInput.tmpiUseFlg]),
                        TemplateDeleteFlg(it[TbTsTmpShoppingInput.deletedFlg])
                    )
                }
        }
    }

    override fun save(
        groupsId: GroupsId,
        templateNo: TemplateNo,
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
    ): ShoppingInputTemplate {
        return transaction {
            TbTsTmpShoppingInput.insert {
                it[TbTsTmpShoppingInput.groupsId] = groupsId.value
                it[TbTsTmpShoppingInput.templateNo] = templateNo.value
                it[TbTsTmpShoppingInput.templateId] = templateId.value
                it[tmpiName] = templateName.value
                it[tmpiMemberId] = memberId.value
                it[tmpiCategoryId] = categoryId.value
                it[tmpiType] = shoppingType.value
                it[tmpiPayment] = shoppingPayment.value
                it[tmpiSettlement] = shoppingSettlement.value
                it[tmpiAmount] = shoppingAmount.value
                it[tmpiRemarks] = shoppingRemarks.value
                it[tmpiUseFlg] = templateUseFlg.value
                it[deletedFlg] = 0
            }

            val shoppingInputTemplate = TbTsTmpShoppingInput.select {
                (TbTsTmpShoppingInput.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingInput.templateNo eq templateNo.value) and
                        (TbTsTmpShoppingInput.templateId eq templateId.value) and
                        (TbTsTmpShoppingInput.tmpiName eq templateName.value) and
                        (TbTsTmpShoppingInput.tmpiMemberId eq memberId.value) and
                        (TbTsTmpShoppingInput.tmpiCategoryId eq categoryId.value) and
                        (TbTsTmpShoppingInput.tmpiType eq shoppingType.value) and
                        (TbTsTmpShoppingInput.tmpiPayment eq shoppingPayment.value) and
                        (TbTsTmpShoppingInput.tmpiSettlement eq shoppingSettlement.value) and
                        (TbTsTmpShoppingInput.tmpiAmount eq shoppingAmount.value) and
                        (TbTsTmpShoppingInput.tmpiRemarks eq shoppingRemarks.value) and
                        (TbTsTmpShoppingInput.tmpiUseFlg eq templateUseFlg.value)
            }.singleOrNull()

            return@transaction shoppingInputTemplate?.let {
                ShoppingInputTemplate(
                    TmpId(it[TbTsTmpShoppingInput.id]),
                    GroupsId(it[TbTsTmpShoppingInput.groupsId]),
                    TemplateNo(it[TbTsTmpShoppingInput.templateNo]),
                    TemplateId(it[TbTsTmpShoppingInput.templateId]),
                    TemplateName(it[TbTsTmpShoppingInput.tmpiName]),
                    MemberId(it[TbTsTmpShoppingInput.tmpiMemberId]),
                    CategoryId(it[TbTsTmpShoppingInput.tmpiCategoryId]),
                    ShoppingType(it[TbTsTmpShoppingInput.tmpiType]),
                    ShoppingPayment(it[TbTsTmpShoppingInput.tmpiPayment]),
                    ShoppingSettlement(it[TbTsTmpShoppingInput.tmpiSettlement]),
                    Amount(it[TbTsTmpShoppingInput.tmpiAmount]),
                    ShoppingRemarks(it[TbTsTmpShoppingInput.tmpiRemarks]),
                    TemplateUseFlg(it[TbTsTmpShoppingInput.tmpiUseFlg]),
                    TemplateDeleteFlg(it[TbTsTmpShoppingInput.deletedFlg])
                )
            } ?: throw IllegalStateException("Failed to save the ShoppingInputTemplate")
        }
    }

    override fun update(
        groupsId: GroupsId,
        templateNo: TemplateNo?,
        templateId: TemplateId,
        templateName: TemplateName?,
        memberId: MemberId?,
        categoryId: CategoryId?,
        shoppingType: ShoppingType?,
        shoppingPayment: ShoppingPayment?,
        shoppingSettlement: ShoppingSettlement?,
        shoppingAmount: Amount?,
        shoppingRemarks: ShoppingRemarks?,
        templateUseFlg: TemplateUseFlg?
    ): Int {
        return transaction {
            val updateRows = TbTsTmpShoppingInput.update({
                (TbTsTmpShoppingInput.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingInput.templateId eq templateId.value)
            }) {
                if (templateNo != null) {
                    it[TbTsTmpShoppingInput.templateNo] = templateNo.value
                }

                if (templateName != null) {
                    it[tmpiName] = templateName.value
                }

                if (memberId != null) {
                    it[tmpiMemberId] = memberId.value
                }

                if (categoryId != null) {
                    it[tmpiCategoryId] = categoryId.value
                }

                if (shoppingType != null) {
                    it[tmpiType] = shoppingType.value
                }

                if (shoppingPayment != null) {
                    it[tmpiPayment] = shoppingPayment.value
                }

                if (shoppingSettlement != null) {
                    it[tmpiSettlement] = shoppingSettlement.value
                }

                if (shoppingAmount != null) {
                    it[tmpiAmount] = shoppingAmount.value
                }

                if (shoppingRemarks != null) {
                    it[tmpiRemarks] = shoppingRemarks.value
                }

                if (templateUseFlg != null) {
                    it[tmpiUseFlg] = templateUseFlg.value
                }

            }
            return@transaction updateRows
        }
    }

    override fun usage(
        groupsId: GroupsId,
        templateId: TemplateId
    ): Int {
        return transaction {
            val updateRows = TbTsTmpShoppingInput.update({
                (TbTsTmpShoppingInput.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingInput.templateId eq templateId.value)
            }) {
                it[tmpiUseFlg] = 1
            }
            return@transaction updateRows
        }
    }

    override fun unUsage(
        groupsId: GroupsId,
        templateId: TemplateId
    ): Int {
        return transaction {
            val updateRows = TbTsTmpShoppingInput.update({
                (TbTsTmpShoppingInput.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingInput.templateId eq templateId.value)
            }) {
                it[tmpiUseFlg] = 0
            }
            return@transaction updateRows
        }
    }

    override fun setDeleted(
        groupsId: GroupsId,
        templateId: TemplateId
    ): Int {
        return transaction {
            val updateRows = TbTsTmpShoppingInput.update({
                (TbTsTmpShoppingInput.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingInput.templateId eq templateId.value)
            }) {
                it[deletedFlg] = 1
            }
            return@transaction updateRows
        }
    }

    override fun setUnDeleted(
        groupsId: GroupsId,
        templateId: TemplateId
    ): Int {
        return transaction {
            val updateRows = TbTsTmpShoppingInput.update({
                (TbTsTmpShoppingInput.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingInput.templateId eq templateId.value)
            }) {
                it[deletedFlg] = 0
            }
            return@transaction updateRows
        }
    }

    override fun delete(groupsId: GroupsId, templateId: TemplateId?): Int {
        return transaction {
            val deleteRows = TbTsTmpShoppingInput.deleteWhere {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    groupsId.let { condition = condition and (TbTsTmpShoppingInput.groupsId eq it.value) }
                    templateId?.let { condition = condition and (TbTsTmpShoppingInput.templateId eq it.value) }
                    condition
                }
            }
            return@transaction deleteRows
        }
    }
}