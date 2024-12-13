package com.alperenmengi.cryptoqueries.model

import com.google.gson.annotations.SerializedName

data class HighlightsResponse(
    val code: Int,
    val message: Message,
    val data: HighlightsData
)

data class Message(
    val type: String,
    val title: String,
    val content: String,
    val action: String
)

data class HighlightsData(
    @SerializedName("tab")
    val tab: String,
    @SerializedName("data")
    val data: List<CryptoData>,
    @SerializedName("filters")
    val filters: HighlightFilters
)

data class CryptoData(
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("market_cap")
    val marketCap: String,
    @SerializedName("fdv_rate")
    val fdvRate: String,
    @SerializedName("24h_percentage")
    val change24h: String,
    @SerializedName("ath")
    val ath: ATHData
)

data class ATHData(
    @SerializedName("price")
    val price: String,
    @SerializedName("date")
    val date: String
)

data class HighlightFilters(
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("updated_timeline")
    val updatedTimeline: String
) 