package com.alperenmengi.cryptoqueries.model

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("userName")
    val userName: String,
    @SerializedName("password")
    val password: String
)

data class RefreshRequest(
    @SerializedName("refreshToken")
    val refreshToken: String
)