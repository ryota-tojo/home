package com.example.home.domain.value_object.user

data class UserId(val value: Int) {
    init {
        requireNotNull(value) { "UserID must not be null." }
    }
}