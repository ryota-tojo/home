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
import com.example.home.domain.value_object.template.TemplateId
import com.example.home.domain.value_object.template.TemplateName
import com.example.home.domain.value_object.template.TemplateUseFlg
import com.example.home.domain.value_object.template.TmpId
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsTmpShoppingInput
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ShoppingInputTemplateRepositoryImpl : ShoppingInputTemplateRepository {

    override fun refer(groupsId: GroupsId?, templateId: TemplateId?): List<ShoppingInputTemplate> {
        return transaction {
            TbTsTmpShoppingInput.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    groupsId?.value?.let { condition = condition and (TbTsTmpShoppingInput.groupsId eq it) }
                    templateId?.value?.let {
                        condition = condition and (TbTsTmpShoppingInput.templateId eq it)
                    } // 仮の条件（適切に修正）
                    condition
                }
            }
                .orderBy(TbTsTmpShoppingInput.id to SortOrder.ASC)
                .map {
                    ShoppingInputTemplate(
                        TmpId(it[TbTsTmpShoppingInput.id]),
                        GroupsId(it[TbTsTmpShoppingInput.groupsId]),
                        TemplateId(it[TbTsTmpShoppingInput.templateId]),
                        TemplateName(it[TbTsTmpShoppingInput.tmpiName]),
                        MemberId(it[TbTsTmpShoppingInput.tmpiMemberId]),
                        CategoryId(it[TbTsTmpShoppingInput.tmpiCategoryId]),
                        ShoppingType(it[TbTsTmpShoppingInput.tmpiType]),
                        ShoppingPayment(it[TbTsTmpShoppingInput.tmpiPayment]),
                        ShoppingSettlement(it[TbTsTmpShoppingInput.tmpiSettlement]),
                        Amount(it[TbTsTmpShoppingInput.tmpiAmount]),
                        ShoppingRemarks(it[TbTsTmpShoppingInput.tmpiRemarks]),
                        TemplateUseFlg(it[TbTsTmpShoppingInput.tmpiUseFlg])
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
    ): ShoppingInputTemplate {
        return transaction {
            TbTsTmpShoppingInput.insert {
                it[TbTsTmpShoppingInput.groupsId] = groupsId.value
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
            }

            val shoppingInputTemplate = TbTsTmpShoppingInput.select {
                (TbTsTmpShoppingInput.groupsId eq groupsId.value) and
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
                    TemplateId(it[TbTsTmpShoppingInput.templateId]),
                    TemplateName(it[TbTsTmpShoppingInput.tmpiName]),
                    MemberId(it[TbTsTmpShoppingInput.tmpiMemberId]),
                    CategoryId(it[TbTsTmpShoppingInput.tmpiCategoryId]),
                    ShoppingType(it[TbTsTmpShoppingInput.tmpiType]),
                    ShoppingPayment(it[TbTsTmpShoppingInput.tmpiPayment]),
                    ShoppingSettlement(it[TbTsTmpShoppingInput.tmpiSettlement]),
                    Amount(it[TbTsTmpShoppingInput.tmpiAmount]),
                    ShoppingRemarks(it[TbTsTmpShoppingInput.tmpiRemarks]),
                    TemplateUseFlg(it[TbTsTmpShoppingInput.tmpiUseFlg])
                )
            } ?: throw IllegalStateException("Failed to save the ShoppingInputTemplate")
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
            val updateRows = TbTsTmpShoppingInput.update({
                (TbTsTmpShoppingInput.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingInput.templateId eq templateId.value)
            }) {
                it[tmpiName] = templateName.value
                it[tmpiMemberId] = memberId.value
                it[tmpiCategoryId] = categoryId.value
                it[tmpiType] = shoppingType.value
                it[tmpiPayment] = shoppingPayment.value
                it[tmpiSettlement] = shoppingSettlement.value
                it[tmpiAmount] = shoppingAmount.value
                it[tmpiRemarks] = shoppingRemarks.value
                it[tmpiUseFlg] = templateUseFlg.value
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