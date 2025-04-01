package com.example.home.datasource.member

import com.example.home.domain.entity.member.Member
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberDeletedFlg
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.infrastructure.persistence.exposed_tables.transaction.TbTsMembers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl : MemberRepository {
    override fun refer(memberId: MemberId?, groupsId: GroupsId?, memberNo: MemberNo?): List<Member> {
        return transaction {
            var condition: Op<Boolean> = Op.TRUE
            if (memberId != null) {
                memberId.let { condition = condition and (TbTsMembers.memberId eq memberId.value) }
            } else {
                groupsId?.let { condition = condition and (TbTsMembers.groupsId eq it.value) }
                memberNo?.let { condition = condition and (TbTsMembers.memberNo eq it.value) }
            }
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
                        MemberName(it[TbTsMembers.memberName]),
                        MemberDeletedFlg(it[TbTsMembers.deletedFlg])
                    )
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
                it[deletedFlg] = 0
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
                    MemberName(it[TbTsMembers.memberName]),
                    MemberDeletedFlg(it[TbTsMembers.deletedFlg])
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

    override fun setDeleted(memberId: MemberId): Int {
        return transaction {
            var condition: Op<Boolean> = TbTsMembers.memberId eq memberId.value
            val updateRows = TbTsMembers.update({
                condition
            }) {
                it[deletedFlg] = 1
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