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


class Repository {
    private val apiService = RetrofitClient.apiService

    // Auth related functions
    suspend fun requestWithAccessToken(userName: String, password: String): Response<AuthResponse> {
        val authRequest = AuthRequest(userName, password)
        return apiService.authtoken(authRequest)
    }

    suspend fun requestWithRefreshToken(refreshToken: String): Response<RefreshResponse> {
        val refreshRequest = RefreshRequest(refreshToken)
        return apiService.refreshtoken(refreshRequest)
    }

    // Market Overview related function
    suspend fun getMarketOverview(): Response<MarketOverviewResponse> {
        return apiService.getMarketOverview()
    }

    // Top Assets related function
    suspend fun getTopAssets(): Response<TopAssetsResponse> {
        return apiService.getTopAssets()
    }

    // ETF Trackers related function
    suspend fun getEtfTrackers(): Response<EtfTrackersResponse> {
        return apiService.getEtfTrackers()
    }

    //Highlights related functions
    suspend fun getTrendingCryptos(): Response<HighlightsResponse> {
        return apiService.getTrendingCryptos()
    }

    suspend fun getTopGainers(): Response<HighlightsResponse> {
        return apiService.getTopGainers()
    }

    suspend fun getNewAthCryptos(): Response<HighlightsResponse> {
        return apiService.getNewAthCryptos()
    }

    suspend fun getRecentlyAddedCryptos(): Response<HighlightsResponse> {
        return apiService.getRecentlyAddedCryptos()
    }
}