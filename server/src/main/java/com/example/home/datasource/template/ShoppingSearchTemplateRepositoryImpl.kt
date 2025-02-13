package com.example.home.datasource.template

import com.example.home.domain.entity.template.ShoppingSearchTemplate
import com.example.home.domain.repository.template.ShoppingSearchTemplateRepository
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
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsTmpShoppingSearch
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class ShoppingSearchTemplateRepositoryImpl : ShoppingSearchTemplateRepository {

    override fun refer(groupsId: GroupsId?, templateId: TemplateId?): List<ShoppingSearchTemplate> {
        return transaction {
            TbTsTmpShoppingSearch.select {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    groupsId?.value?.let { condition = condition and (TbTsTmpShoppingSearch.groupsId eq it) }
                    templateId?.value?.let {
                        condition = condition and (TbTsTmpShoppingSearch.templateId eq it)
                    } // 仮の条件（適切に修正）
                    condition
                }
            }
                .orderBy(TbTsTmpShoppingSearch.id to SortOrder.ASC)
                .map {
                    ShoppingSearchTemplate(
                        TmpId(it[TbTsTmpShoppingSearch.id]),
                        GroupsId(it[TbTsTmpShoppingSearch.groupsId]),
                        TemplateId(it[TbTsTmpShoppingSearch.templateId]),
                        TemplateName(it[TbTsTmpShoppingSearch.tmpsName]),
                        MemberId(it[TbTsTmpShoppingSearch.tmpsMemberId]),
                        CategoryId(it[TbTsTmpShoppingSearch.tmpsCategoryId]),
                        ShoppingType(it[TbTsTmpShoppingSearch.tmpsType]),
                        ShoppingPayment(it[TbTsTmpShoppingSearch.tmpsPayment]),
                        ShoppingSettlement(it[TbTsTmpShoppingSearch.tmpsSettlement]),
                        Amount(it[TbTsTmpShoppingSearch.tmpsMinAmount]),
                        Amount(it[TbTsTmpShoppingSearch.tmpsMaxAmount]),
                        ShoppingRemarks(it[TbTsTmpShoppingSearch.tmpsRemarks]),
                        TemplateUseFlg(it[TbTsTmpShoppingSearch.tmpsUseFlg])
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
        shoppingMinAmount: Amount,
        shoppingMaxAmount: Amount,
        shoppingRemarks: ShoppingRemarks,
        templateUseFlg: TemplateUseFlg
    ): ShoppingSearchTemplate {
        return transaction {
            TbTsTmpShoppingSearch.insert {
                it[TbTsTmpShoppingSearch.groupsId] = groupsId.value
                it[TbTsTmpShoppingSearch.templateId] = templateId.value
                it[tmpsName] = templateName.value
                it[tmpsMemberId] = memberId.value
                it[tmpsCategoryId] = categoryId.value
                it[tmpsType] = shoppingType.value
                it[tmpsPayment] = shoppingPayment.value
                it[tmpsSettlement] = shoppingSettlement.value
                it[tmpsMinAmount] = shoppingMinAmount.value
                it[tmpsMaxAmount] = shoppingMaxAmount.value
                it[tmpsRemarks] = shoppingRemarks.value
                it[tmpsUseFlg] = templateUseFlg.value
            }

            val shoppingInputTemplate = TbTsTmpShoppingSearch.select {
                (TbTsTmpShoppingSearch.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingSearch.templateId eq templateId.value) and
                        (TbTsTmpShoppingSearch.tmpsName eq templateName.value) and
                        (TbTsTmpShoppingSearch.tmpsMemberId eq memberId.value) and
                        (TbTsTmpShoppingSearch.tmpsCategoryId eq categoryId.value) and
                        (TbTsTmpShoppingSearch.tmpsType eq shoppingType.value) and
                        (TbTsTmpShoppingSearch.tmpsPayment eq shoppingPayment.value) and
                        (TbTsTmpShoppingSearch.tmpsSettlement eq shoppingSettlement.value) and
                        (TbTsTmpShoppingSearch.tmpsMinAmount eq shoppingMinAmount.value) and
                        (TbTsTmpShoppingSearch.tmpsMaxAmount eq shoppingMaxAmount.value) and
                        (TbTsTmpShoppingSearch.tmpsRemarks eq shoppingRemarks.value) and
                        (TbTsTmpShoppingSearch.tmpsUseFlg eq templateUseFlg.value)
            }.singleOrNull()

            return@transaction shoppingInputTemplate?.let {
                ShoppingSearchTemplate(
                    TmpId(it[TbTsTmpShoppingSearch.id]),
                    GroupsId(it[TbTsTmpShoppingSearch.groupsId]),
                    TemplateId(it[TbTsTmpShoppingSearch.templateId]),
                    TemplateName(it[TbTsTmpShoppingSearch.tmpsName]),
                    MemberId(it[TbTsTmpShoppingSearch.tmpsMemberId]),
                    CategoryId(it[TbTsTmpShoppingSearch.tmpsCategoryId]),
                    ShoppingType(it[TbTsTmpShoppingSearch.tmpsType]),
                    ShoppingPayment(it[TbTsTmpShoppingSearch.tmpsPayment]),
                    ShoppingSettlement(it[TbTsTmpShoppingSearch.tmpsSettlement]),
                    Amount(it[TbTsTmpShoppingSearch.tmpsMinAmount]),
                    Amount(it[TbTsTmpShoppingSearch.tmpsMaxAmount]),
                    ShoppingRemarks(it[TbTsTmpShoppingSearch.tmpsRemarks]),
                    TemplateUseFlg(it[TbTsTmpShoppingSearch.tmpsUseFlg])
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
        shoppingMinAmount: Amount,
        shoppingMaxAmount: Amount,
        shoppingRemarks: ShoppingRemarks,
        templateUseFlg: TemplateUseFlg
    ): Int {
        return transaction {
            val updateRows = TbTsTmpShoppingSearch.update({
                (TbTsTmpShoppingSearch.groupsId eq groupsId.value) and
                        (TbTsTmpShoppingSearch.templateId eq templateId.value)
            }) {
                it[tmpsName] = templateName.value
                it[tmpsMemberId] = memberId.value
                it[tmpsCategoryId] = categoryId.value
                it[tmpsType] = shoppingType.value
                it[tmpsPayment] = shoppingPayment.value
                it[tmpsSettlement] = shoppingSettlement.value
                it[tmpsMinAmount] = shoppingMinAmount.value
                it[tmpsMaxAmount] = shoppingMaxAmount.value
                it[tmpsRemarks] = shoppingRemarks.value
                it[tmpsUseFlg] = templateUseFlg.value
            }
            return@transaction updateRows
        }
    }

    override fun delete(groupsId: GroupsId, templateId: TemplateId?): Int {
        return transaction {
            val deleteRows = TbTsTmpShoppingSearch.deleteWhere {
                Op.build {
                    var condition: Op<Boolean> = Op.TRUE
                    groupsId.let { condition = condition and (TbTsTmpShoppingSearch.groupsId eq it.value) }
                    templateId?.let { condition = condition and (TbTsTmpShoppingSearch.templateId eq it.value) }
                    condition
                }
            }
            return@transaction deleteRows
        }
    }
}