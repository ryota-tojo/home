package com.example.home.domain.entity.comment.result

import com.example.home.domain.entity.comment.Comment

data class CommentSaveResult(
    val result: String,
    val category: Comment? = null
)
