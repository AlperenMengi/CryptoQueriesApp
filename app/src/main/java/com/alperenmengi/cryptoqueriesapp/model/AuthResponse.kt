package com.alperenmengi.cryptoqueries.model

data class AuthResponse(
    val code: Int,
    val data: AuthData
)

data class AuthData(
    val accessToken: String,
    val refreshToken: String
)

data class RefreshResponse(
    val code: Int,
    val data: RefreshData
)

data class RefreshData(
    val accessToken: String,
    val refreshToken: String
)