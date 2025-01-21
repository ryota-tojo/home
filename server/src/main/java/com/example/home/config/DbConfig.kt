package com.example.home.config

data class DbConfig(
    var url: String,
    var user: String,
    var password: String
) {
    init {
        require(!url.isNullOrEmpty()) { "URL must not be null or empty" }
        require(!user.isNullOrEmpty()) { "User must not be null or empty" }
        require(!password.isNullOrEmpty()) { "Password must not be null or empty" }
    }
}
