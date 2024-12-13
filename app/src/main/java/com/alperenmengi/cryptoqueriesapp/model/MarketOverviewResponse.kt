package com.alperenmengi.cryptoqueriesapp.model

import com.google.gson.annotations.SerializedName

data class MarketOverviewResponse(
    val code: Int,
    val data: MarketData
)

data class MarketData(
    @SerializedName("market_cap")
    val marketCap: MarketCapData,
    @SerializedName("volume")
    val volume: VolumeData,
    @SerializedName("altcoin_index")
    val altcoinIndex: AltcoinIndexData,
    @SerializedName("fear_and_greed_index")
    val fearAndGreedIndex: FearAndGreedIndexData
)

data class MarketCapData(
    val value: String,
    @SerializedName("change_percentage")
    val changePercentage: String
)

data class VolumeData(
    val value: String,
    @SerializedName("change_percentage")
    val changePercentage: String
)

data class AltcoinIndexData(
    val score: Int,
    @SerializedName("max_score")
    val maxScore: Int
)

data class FearAndGreedIndexData(
    val score: Int,
    val scale: String
)