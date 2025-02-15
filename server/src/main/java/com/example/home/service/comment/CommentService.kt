package com.example.home.service.comment

import com.example.home.domain.entity.comment.result.CommentDeleteResult
import com.example.home.domain.entity.comment.result.CommentReferResult
import com.example.home.domain.entity.comment.result.CommentSaveResult
import com.example.home.domain.entity.comment.result.CommentUpdateResult
import com.example.home.domain.model.ResponseCode
import com.example.home.domain.repository.comment.CommentRepository
import com.example.home.domain.value_object.comment.Content
import com.example.home.domain.value_object.etc.MM
import com.example.home.domain.value_object.etc.YYYY
import com.example.home.domain.value_object.group.GroupsId
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository
) {

    fun refer(
        groupsId: GroupsId? = null,
        yyyy: YYYY? = null,
        mm: MM? = null
    ): CommentReferResult {
        val commentList = commentRepository.refer(groupsId, yyyy, mm)
        return CommentReferResult(
            ResponseCode.成功.code,
            commentList
        )
    }

    fun save(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM,
        content: Content
    ): CommentSaveResult {
        val comment = commentRepository.save(groupsId, yyyy, mm, content)
        return CommentSaveResult(
            ResponseCode.成功.code,
            comment
        )
    }

    fun update(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM,
        content: Content
    ): CommentUpdateResult {
        val updateRows = commentRepository.update(groupsId, yyyy, mm, content)
        if (updateRows == 0) {
            return CommentUpdateResult(
                ResponseCode.データ不在エラー.code,
                updateRows
            )
        }
        return CommentUpdateResult(
            ResponseCode.成功.code,
            updateRows
        )
    }

    fun delete(
        groupsId: GroupsId,
        yyyy: YYYY,
        mm: MM
    ): CommentDeleteResult {
        val deleteRows = commentRepository.delete(groupsId, yyyy, mm)
        if (deleteRows == 0) {
            return CommentDeleteResult(
                ResponseCode.データ不在エラー.code,
                deleteRows
            )
        }
        return CommentDeleteResult(
            ResponseCode.成功.code,
            deleteRows
        )
    }
}