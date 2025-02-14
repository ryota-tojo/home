package com.example.home.datasource.member

import com.example.home.domain.entity.category.Category
import com.example.home.domain.entity.member.Member
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.value_object.category.CategoryId
import com.example.home.domain.value_object.category.CategoryName
import com.example.home.domain.value_object.category.CategoryNo
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsCategorys
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsMembers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl : MemberRepository {
    override fun refer(memberId: MemberId?, groupsId: GroupsId?, memberNo: MemberNo?): List<Member> {
        return transaction {
            if (memberId == null && groupsId == null && memberNo == null) {
                TbTsMembers
                    .selectAll()
                    .orderBy(TbTsMembers.memberNo to SortOrder.ASC)
                    .map {
                        Member(
                            MemberId(it[TbTsMembers.memberId]),
                            GroupsId(it[TbTsMembers.groupsId]),
                            MemberNo(it[TbTsMembers.memberNo]),
                            MemberName(it[TbTsMembers.memberName])
                        )
                    }
            } else if (memberId != null) {
                var condition: Op<Boolean> = TbTsMembers.memberId eq memberId.value

                TbTsCategorys
                    .select {
                        condition
                    }
                    .map {
                        Member(
                            MemberId(it[TbTsMembers.memberId]),
                            GroupsId(it[TbTsMembers.groupsId]),
                            MemberNo(it[TbTsMembers.memberNo]),
                            MemberName(it[TbTsMembers.memberName])
                        )
                    }
            } else {
                var condition: Op<Boolean> = Op.TRUE
                groupsId?.let { condition = condition and (TbTsMembers.groupsId eq it.value) }
                memberNo?.let { condition = condition and (TbTsMembers.memberNo eq it.value) }

                TbTsMembers
                    .select {
                        condition
                    }
                    .orderBy(TbTsMembers.memberNo to SortOrder.ASC)
                    .map {
                        Member(
                            MemberId(it[TbTsMembers.memberId]),
                            GroupsId(it[TbTsMembers.groupsId]),
                            MemberNo(it[TbTsMembers.memberNo]),
                            MemberName(it[TbTsMembers.memberName])
                        )
                    }
            }
        }
    }

    override fun save(
        groupsId: GroupsId,
        memberNo: MemberNo,
        memberName: MemberName
    ): Member {
        return transaction {
            TbTsMembers.insert {
                it[TbTsMembers.groupsId] = groupsId.value
                it[TbTsMembers.memberNo] = memberNo.value
                it[TbTsMembers.memberName] = memberName.value
            }

            val member = TbTsMembers.select {
                (TbTsMembers.groupsId eq groupsId.value) and
                        (TbTsMembers.memberNo eq memberNo.value) and
                        (TbTsMembers.memberName eq memberName.value)
            }.singleOrNull()

            return@transaction member?.let {
                Member(
                    MemberId(it[TbTsMembers.memberId]),
                    GroupsId(it[TbTsMembers.groupsId]),
                    MemberNo(it[TbTsMembers.memberNo]),
                    MemberName(it[TbTsMembers.memberName])
                )
            } ?: throw IllegalStateException("Failed to save the Member")
        }
    }

    override fun update(
        memberId: MemberId,
        memberNo: MemberNo?,
        memberName: MemberName?
    ): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsMembers.memberId eq memberId.value
            memberNo?.let { condition = condition and (TbTsMembers.memberNo eq it.value) }
            memberName?.let { condition = condition and (TbTsMembers.memberName eq it.value) }
            val updateRows = TbTsMembers.update({
                condition
            }) {
                if (memberNo != null) {
                    it[TbTsMembers.memberNo] = memberNo.value
                }
                if (memberName != null) {
                    it[TbTsMembers.memberName] = memberName.value
                }
            }
            return@transaction updateRows
        }
    }

    override fun delete(groupsId: GroupsId?, memberId: MemberId?): Int {
        return transaction {
            val condition = if (groupsId != null) {
                TbTsMembers.groupsId eq groupsId.value
            } else {
                if (memberId != null) {
                    TbTsMembers.memberId eq memberId.value
                } else {
                    return@transaction 0
                }
            }
            val deleteRows = TbTsMembers.deleteWhere { condition }
            return@transaction deleteRows
        }
    }

}