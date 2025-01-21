package com.example.home.domain.value_object.user

data class UserApprovalFlg(val value: Int) {
    companion object {
        val MIN = 0
        val MAX = 1
    }

    init {
        require(value in MIN..MAX) { "UserApprovalFlg must be in range $MIN..$MAX" }
    }
}