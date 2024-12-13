package com.alperenmengi.cryptoqueriesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperenmengi.cryptoqueries.model.AuthResponse
import com.alperenmengi.cryptoqueries.model.EtfTrackersResponse
import com.alperenmengi.cryptoqueries.model.HighlightsResponse
import com.alperenmengi.cryptoqueries.model.TopAssetsResponse
import com.alperenmengi.cryptoqueriesapp.api.Repository
import com.alperenmengi.cryptoqueriesapp.model.*
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    private val repository = Repository()

    private val _marketOverview = MutableLiveData<MarketOverviewResponse>()
    val marketOverview: LiveData<MarketOverviewResponse> = _marketOverview

    private val _authResponse = MutableLiveData<AuthResponse>()
    val authResponse: LiveData<AuthResponse> = _authResponse

    private val _tokenEvent = MutableLiveData<TokenEvent>()
    val tokenEvent: LiveData<TokenEvent> = _tokenEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _clearTokenEvent = MutableLiveData<Unit>()
    val clearTokenEvent: LiveData<Unit> = _clearTokenEvent

    private val _topAssets = MutableLiveData<TopAssetsResponse>()
    val topAssets: LiveData<TopAssetsResponse> = _topAssets

    private val _etfTrackers = MutableLiveData<EtfTrackersResponse>()
    val etfTrackers: LiveData<EtfTrackersResponse> = _etfTrackers

    private val _highlights = MutableLiveData<HighlightsResponse>()
    val highlights: LiveData<HighlightsResponse> = _highlights

    init {
        performGetAccessToken()
    }

    //ACCESS TOKEN
    private fun performGetAccessToken() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.requestWithAccessToken("reguser@nft.com", "heremynft@321")
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse?.code == 200) {
                        authResponse.data.accessToken?.let { token ->
                            handleSuccessfulGetAccessToken(token, authResponse.data.refreshToken)
                        }
                    }
                } else {
                    handleFailedGetAccessToken() // accessToken alınamazsa refresh token ile yeni bir token çifti alınma işlemi gerçekleşecek.
                    _error.value = "Authentication failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Authentication error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleSuccessfulGetAccessToken(accessToken: String, refreshToken: String) {
        // Emit token event for Fragment to handle
        _tokenEvent.value = TokenEvent(accessToken, refreshToken)
        
        // Fetch all data
        getMarketOverview()
        getTopAssets()
        getEtfTrackers()
    }

    // Access Token alımı başarısız olursa şayet bu sefer refresh token ile bir post gönderimi olacak ve geriye yeni bir accessToken, refreshToken çifti alınacak.
    private fun handleFailedGetAccessToken(){
        _clearTokenEvent.value = Unit
        performGetRefreshToken()
    }

    // REFRESH TOKEN
    private fun performGetRefreshToken() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.requestWithRefreshToken("REFRESH TOKEN GELECEK")
                if (response.isSuccessful) {
                    val refreshResponse = response.body()
                    if (refreshResponse?.code == 200) {
                        refreshResponse.data.accessToken?.let { token ->
                            handleSuccessfulGetRefreshToken(token, refreshResponse.data.refreshToken)
                        }
                    }
                } else {
                    _error.value = "Authentication failed: ${response.code()}" // error olduğunda toast mesajı yazdırılıyor.
                }
            } catch (e: Exception) {
                _error.value = "Authentication error: ${e.message}"// error olduğunda toast mesajı yazdırılıyor.
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleSuccessfulGetRefreshToken(accessToken: String, refreshToken: String) {
        // Emit token event for Fragment to handle
        _tokenEvent.value = TokenEvent(accessToken, refreshToken)

        // Fetch all data
        getMarketOverview()
        getTopAssets()
        getEtfTrackers()
    }

    fun getMarketOverview() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getMarketOverview()
                if (response.isSuccessful) {
                    response.body()?.let { marketOverview ->
                        _marketOverview.value = marketOverview
                    }
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTopAssets() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getTopAssets()
                if (response.isSuccessful) {
                    response.body()?.let { topAssets ->
                        _topAssets.value = topAssets
                    }
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getEtfTrackers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getEtfTrackers()
                if (response.isSuccessful) {
                    response.body()?.let { etfTrackers ->
                        _etfTrackers.value = etfTrackers
                    }
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    enum class HighlightType {
        TRENDINGS, TOP_GAINERS, NEW_ATH, RECENTLY_ADDED
    }

    fun getHighlights(type: HighlightType) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = when (type) {
                    HighlightType.TRENDINGS -> repository.getTrendingCryptos()
                    HighlightType.TOP_GAINERS -> repository.getTopGainers()
                    HighlightType.NEW_ATH -> repository.getNewAthCryptos()
                    HighlightType.RECENTLY_ADDED -> repository.getRecentlyAddedCryptos()
                }

                if (response.isSuccessful) {
                    response.body()?.let { highlights ->
                        _highlights.value = highlights
                    }
                } else {
                    _error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    data class TokenEvent(
        val accessToken: String,
        val refreshToken: String
    )
} 