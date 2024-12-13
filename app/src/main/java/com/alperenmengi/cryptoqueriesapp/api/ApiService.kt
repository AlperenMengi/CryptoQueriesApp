package com.alperenmengi.cryptoqueriesapp.api

import com.alperenmengi.cryptoqueries.model.AuthRequest
import com.alperenmengi.cryptoqueries.model.AuthResponse
import com.alperenmengi.cryptoqueries.model.EtfTrackersResponse
import com.alperenmengi.cryptoqueries.model.HighlightsResponse
import com.alperenmengi.cryptoqueries.model.RefreshRequest
import com.alperenmengi.cryptoqueries.model.RefreshResponse
import com.alperenmengi.cryptoqueries.model.TopAssetsResponse
import com.alperenmengi.cryptoqueriesapp.model.MarketOverviewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/token")
    suspend fun authtoken(@Body request: AuthRequest): Response<AuthResponse>

    @POST("/auth/refreshtoken")
    suspend fun refreshtoken(@Body request: RefreshRequest): Response<RefreshResponse>

    @GET("v1/market-overview")
    suspend fun getMarketOverview(): Response<MarketOverviewResponse>

    @GET("v1/top-assets")
    suspend fun getTopAssets(): Response<TopAssetsResponse>

    @GET("v1/etf-trackers")
    suspend fun getEtfTrackers(): Response<EtfTrackersResponse>

    @GET("v1/highlights/trendings")
    suspend fun getTrendingCryptos(): Response<HighlightsResponse>

    @GET("v1/highlights/top-gainers")
    suspend fun getTopGainers(): Response<HighlightsResponse>

    @GET("v1/highlights/new-ath")
    suspend fun getNewAthCryptos(): Response<HighlightsResponse>

    @GET("v1/highlights/recently-added")
    suspend fun getRecentlyAddedCryptos(): Response<HighlightsResponse>
} 