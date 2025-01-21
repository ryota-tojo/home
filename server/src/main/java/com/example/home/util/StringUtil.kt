package com.example.home.util

object StringUtil {

    fun byteSize(key :String): Int {
        return key.toByteArray(Charsets.UTF_8).size
    }

}