package com.alperenmengi.cryptoqueries.model

import com.google.gson.annotations.SerializedName

data class TopAssetsResponse(
    val code: Int,
    val data: TopAssetsData
)

data class TopAssetsData(
    val cryptocurrencies: List<CryptocurrencyData>
)

data class CryptocurrencyData(
    @SerializedName("dominance")
    val dominance: String,
    @SerializedName("price")
    val price: String
) 