package com.alperenmengi.cryptoqueriesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alperenmengi.cryptoqueriesapp.R
import com.alperenmengi.cryptoqueriesapp.api.Repository
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val repository = Repository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        loadInitialData()
    }

    private fun loadInitialData() {
        lifecycleScope.launch {
            try {
                // Get access token first
                val tokenResponse = repository.requestWithAccessToken("reguser@nft.com", "heremynft@321")
                if (tokenResponse.isSuccessful) {
                    val authResponse = tokenResponse.body()
                    if (authResponse?.code == 200) {
                        authResponse.data.accessToken?.let { token ->
                            // Save tokens and proceed to MainActivity
                            startMainActivity()
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle error - maybe show a retry button
                e.printStackTrace()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}