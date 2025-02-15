package com.example.home.domain.entity.comment.result

import com.example.home.domain.entity.comment.Comment

data class CommentReferResult(
    val result: String,
    val category: List<Comment>? = null
)
