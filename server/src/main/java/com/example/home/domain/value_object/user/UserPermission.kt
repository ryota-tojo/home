package com.example.home.domain.value_object.user

data class UserPermission(val value: Int) {
    companion object {
        val MIN = 0
        val MAX = 2
    }

    init {
        require(value in MIN..MAX) { "value must be in range $MIN..$MAX" }
    }
}