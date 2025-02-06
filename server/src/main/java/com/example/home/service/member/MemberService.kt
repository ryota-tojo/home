package com.example.home.service.member

import com.example.home.datasource.member.MemberRepositoryImpl
import com.example.home.domain.entity.member.result.MemberDeleteResult
import com.example.home.domain.entity.member.result.MemberReferResult
import com.example.home.domain.entity.member.result.MemberSaveResult
import com.example.home.domain.entity.member.result.MemberUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.util.ValidationCheck
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository = MemberRepositoryImpl()
) {
    fun refer(groupsId: GroupsId): MemberReferResult {
        val MemberList = memberRepository.refer(groupsId)
        if (MemberList == null) {
            return MemberReferResult(
                String.format(ResponseCode.成功_条件付き.code, "MEMBER_NOT_FOUND"),
                null
            )
        }
        return MemberReferResult(
            ResponseCode.成功.code,
            MemberList
        )
    }

    fun save(groupsId: GroupsId, memberNo: MemberNo, memberName: MemberName): MemberSaveResult {
        if (!ValidationCheck.symbol(memberName.toString()).result) {
            return MemberSaveResult(
                ResponseCode.バリデーションエラー.code,
                null
            )
        }
        val member = memberRepository.save(groupsId, memberNo, memberName)
        return MemberSaveResult(
            ResponseCode.成功.code,
            member
        )
    }

    fun update(groupsId: GroupsId, memberNo: MemberNo, memberName: MemberName): MemberUpdateResult {
        if (!ValidationCheck.symbol(memberName.toString()).result) {
            return MemberUpdateResult(
                ResponseCode.バリデーションエラー.code,
                0
            )
        }
        val updateRows = memberRepository.update(groupsId, memberNo, memberName)
        if (updateRows == 0) {
            return MemberUpdateResult(
                ResponseCode.データ不在エラー.code,
                updateRows
            )
        }
        return MemberUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(groupsId: GroupsId, memberNo: MemberNo?): MemberDeleteResult {
        val deleteRows = if (memberNo != null) {
            memberRepository.delete(groupsId, memberNo)
        } else {
            memberRepository.delete(groupsId)
        }
        if (deleteRows == 0) {
            return MemberDeleteResult(
                ResponseCode.データ不在エラー.code,
                deleteRows
            )
        }
        return MemberDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }
}