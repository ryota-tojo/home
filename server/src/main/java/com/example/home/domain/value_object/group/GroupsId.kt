package com.example.home.domain.value_object.group

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

data class GroupsId(val value :String) {
    companion object {
        val MAX_BYTE_LENGTH = 64;
    }
    init {
        requireNotNull(value) { "GroupsID must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "GroupsID must not exceed $MAX_BYTE_LENGTH bytes. Actual size: ${StringUtil.byteSize(value)} bytes."
        }
    }
}