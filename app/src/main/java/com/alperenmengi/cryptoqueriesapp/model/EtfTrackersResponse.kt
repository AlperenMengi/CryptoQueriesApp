package com.alperenmengi.cryptoqueries.model

import com.google.gson.annotations.SerializedName

data class EtfTrackersResponse(
    val code: Int,
    val data: EtfTrackersData
)

data class EtfTrackersData(
    @SerializedName("etf_trackers")
    val etfTrackers: EtfTrackers
)

data class EtfTrackers(
    @SerializedName("date")
    val date: String,
    @SerializedName("daily_total")
    val dailyTotal: String,
    @SerializedName("btc_etf")
    val btcEtf: String,
    @SerializedName("ethereum_etf")
    val ethereumEtf: String
)