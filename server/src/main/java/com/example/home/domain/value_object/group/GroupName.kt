package com.example.home.domain.value_object.group

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

data class GroupName(val value: String) {
    companion object{
        val MAX_BYTE_LENGTH = 64;
    }
    init {
        requireNotNull(value) { "GroupName must not be null." }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH){
            "GroupName must not exceed $MAX_BYTE_LENGTH bytes. Actual size: ${StringUtil.byteSize(value)} bytes."
        }
        require(Constants.INVALID_SYMBOL.none {  it in value }) {
            "GroupName must not contain forbidden characters: ${Constants.INVALID_SYMBOL.joinToString(", ")}"
        }
    }
}