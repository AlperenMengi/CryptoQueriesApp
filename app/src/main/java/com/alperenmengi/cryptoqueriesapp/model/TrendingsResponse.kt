package com.alperenmengi.cryptoqueries.model

import com.google.gson.annotations.SerializedName

data class TrendingsResponse(
    val code: Int,
    val data: TrendingsData
)

data class TrendingsData(
    @SerializedName("tab")
    val tab: String,
    @SerializedName("data")
    val data: List<TrendingCrypto>,
    @SerializedName("filters")
    val filters: TrendingFilters
)

data class TrendingCrypto(
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("market_cap")
    val marketCap: String,
    @SerializedName("24h_percentage")
    val change24h: String,
    @SerializedName("ath")
    val ath: TrendingsATHData
)

data class TrendingsATHData(
    @SerializedName("price")
    val price: String,
    @SerializedName("date")
    val date: String
)

data class TrendingFilters(
    @SerializedName("market_cap_rank")
    val marketCapRank: Int,
    @SerializedName("updated_timeline")
    val updatedTimeline: String
) 