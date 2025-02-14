package com.example.home.service.member

import com.example.home.domain.entity.member.result.MemberDeleteResult
import com.example.home.domain.entity.member.result.MemberReferResult
import com.example.home.domain.entity.member.result.MemberSaveResult
import com.example.home.domain.entity.member.result.MemberUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.member.MemberRepository
import com.example.home.domain.value_object.group.GroupsId
import com.example.home.domain.value_object.member.MemberId
import com.example.home.domain.value_object.member.MemberName
import com.example.home.domain.value_object.member.MemberNo
import com.example.home.util.ValidationCheck
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun refer(memberId: MemberId? = null, groupsId: GroupsId? = null, memberNo: MemberNo? = null): MemberReferResult {
        val MemberList = memberRepository.refer(memberId, groupsId, memberNo)
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

    fun update(memberId: MemberId, memberNo: MemberNo? = null, memberName: MemberName? = null): MemberUpdateResult {
        if (memberName != null) {
            if (!ValidationCheck.symbol(memberName.toString()).result) {
                return MemberUpdateResult(
                    ResponseCode.バリデーションエラー.code,
                    0
                )
            }
        }
        val updateRows = memberRepository.update(memberId, memberNo, memberName)
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

    fun delete(groupsId: GroupsId? = null, memberId: MemberId? = null): MemberDeleteResult {
        val deleteRows = memberRepository.delete(groupsId, memberId)
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