package com.example.home.domain.value_object.template

import com.example.home.domain.value_object.Constants
import com.example.home.util.StringUtil

data class TemplateName(val value: String){
    companion object{
        val MAX_BYTE_LENGTH = 64;
    }
    init{
        requireNotNull(value) { "TemplateName must not be empty" }
        require(StringUtil.byteSize(value) <= MAX_BYTE_LENGTH) {
            "TemplateName must not exceed ${MAX_BYTE_LENGTH} bytes, actual size: ${StringUtil.byteSize(value)} bytes"
        }
        require(Constants.INVALID_SYMBOL.none {  it in value }) {
            "TemplateName must not contain forbidden characters: ${Constants.INVALID_SYMBOL.joinToString(", ")}"
        }
    }
}
