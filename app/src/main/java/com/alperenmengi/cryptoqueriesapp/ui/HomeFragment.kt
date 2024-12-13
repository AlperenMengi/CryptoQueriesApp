package com.alperenmengi.cryptoqueriesapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperenmengi.cryptoqueries.model.CryptocurrencyData
import com.alperenmengi.cryptoqueries.model.EtfTrackers
import com.alperenmengi.cryptoqueriesapp.R
import com.alperenmengi.cryptoqueriesapp.adapter.CryptoAdapter
import com.alperenmengi.cryptoqueriesapp.model.MarketData
import com.alperenmengi.cryptoqueriesapp.databinding.FragmentHomeBinding
import com.alperenmengi.cryptoqueriesapp.util.DateFormatter
import com.alperenmengi.cryptoqueriesapp.viewmodel.SharedViewModel
import com.alperenmengi.cryptoqueriesapp.util.TokenManager
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var cryptoAdapter: CryptoAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSpinners()
        setupTabs()
        setupObservers()

        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
        viewModel.getHighlights(SharedViewModel.HighlightType.TRENDINGS)
    }

    private fun setupRecyclerView() {
        cryptoAdapter = CryptoAdapter()
        binding.cryptoRecyclerView.apply {
            adapter = cryptoAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> viewModel.getHighlights(SharedViewModel.HighlightType.TRENDINGS)
                    1 -> viewModel.getHighlights(SharedViewModel.HighlightType.TOP_GAINERS)
                    2 -> viewModel.getHighlights(SharedViewModel.HighlightType.NEW_ATH)
                    3 -> viewModel.getHighlights(SharedViewModel.HighlightType.RECENTLY_ADDED)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    //SETUP OBSERVERS
    private fun setupObservers() {
        //MarketOverview
        viewModel.marketOverview.observe(viewLifecycleOwner) { response ->
            updateMarketOverviewUI(response.data)
        }
        //TopAssets
        viewModel.topAssets.observe(viewLifecycleOwner) { response ->
            updateTopAssetsUI(response.data.cryptocurrencies)
        }
        //Error Messages Handeling
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Show/hide loading indicator
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        //AccessToken Alınabildiyse Yapılacak İşlem
        viewModel.tokenEvent.observe(viewLifecycleOwner) { event ->
            context?.let { context ->
                // Save tokens
                TokenManager.saveAccessToken(context, event.accessToken)
                TokenManager.saveRefreshToken(context, event.refreshToken)
                
                Toast.makeText(context, "GetAccessToken is successful! Token: ${event.accessToken}", Toast.LENGTH_LONG).show()
                println("ACCESS TOKEN: ${event.accessToken}")
                
                val savedAccessToken = TokenManager.getAccessToken(context)
                val savedRefreshToken = TokenManager.getRefreshToken(context)
                println("Saved ACCESS TOKEN: $savedAccessToken")
                println("Saved REFRESH TOKEN: $savedRefreshToken")
            }
        }
        // Yeni token çifti alındığında eski accesstokeni temizleme
        viewModel.clearTokenEvent.observe(viewLifecycleOwner) {
            context?.let { context ->
                TokenManager.clearAccessToken(context)
            }
        }

        viewModel.etfTrackers.observe(viewLifecycleOwner) { response ->
            updateEtfTrackersUI(response.data.etfTrackers)
        }

        viewModel.highlights.observe(viewLifecycleOwner) { response ->
            if (response.code == 200) {
                cryptoAdapter.updateList(response.data.data)
            }
        }
    }
    // UI GÜNCELLEME İŞLEMLERİ
    private fun updateMarketOverviewUI(marketData: MarketData) {
        with(binding) {
            marketCapPriceText.text = marketData.marketCap.value
            marketCapPercentageText.text = marketData.marketCap.changePercentage
            volumePriceText.text = marketData.volume.value
            volumePercentageText.text = marketData.volume.changePercentage
            altcoinIndexScoreText.text = marketData.altcoinIndex.score.toString()
            altcoinIndexMaxScoreText.text = marketData.altcoinIndex.maxScore.toString()
            fearGreedValueText.text = marketData.fearAndGreedIndex.score.toString()
            fearGreedProgressBar.progress = marketData.fearAndGreedIndex.score
            updateArrowIcons(marketData)
        }
    }
    private fun updateArrowIcons(marketData: MarketData) {
        // Update market cap arrow
        val isMarketCapPositive = !marketData.marketCap.changePercentage.startsWith("-")
        binding.marketCapPercentageText.setTextColor(if (isMarketCapPositive) Color.GREEN else Color.RED)
        binding.marketCapArrow.setImageResource(if (isMarketCapPositive) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)

        // Update volume arrow
        val isVolumePositive = !marketData.volume.changePercentage.startsWith("-")
        binding.volumePercentageText.setTextColor(if (isVolumePositive) Color.GREEN else Color.RED)
        binding.volumeArrow.setImageResource(if (isVolumePositive) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
    }

    private fun updateTopAssetsUI(cryptocurrencies: List<CryptocurrencyData>) {
        with(binding) {
            // Assuming first item is Bitcoin and second is Ethereum
            cryptocurrencies.getOrNull(0)?.let { bitcoin ->
                bitcoinDText.text = "Dm: ${bitcoin.dominance}"
                bitcoinPText.text = "Pr: ${bitcoin.price}"
            }
            
            cryptocurrencies.getOrNull(1)?.let { ethereum ->
                ethereumDText.text = "Dm: ${ethereum.dominance}"
                ethereumPText.text = "Pr: ${ethereum.price}"
            }
        }
    }

    private fun updateEtfTrackersUI(etfTrackers: EtfTrackers) {
        with(binding) {
            dateText.text = DateFormatter.formatDate(etfTrackers.date)
            daileyTotalValue.text = etfTrackers.dailyTotal
            btcEtfValue.text = etfTrackers.btcEtf
            ethereumEtfValue.text = etfTrackers.ethereumEtf

            // Set text colors based on negative values
            daileyTotalValue.setTextColor(if (etfTrackers.dailyTotal.startsWith("-"))
                Color.RED else Color.GREEN)
            btcEtfValue.setTextColor(if (etfTrackers.btcEtf.startsWith("-"))
                Color.RED else Color.GREEN)
            ethereumEtfValue.setTextColor(if (etfTrackers.ethereumEtf.startsWith("-"))
                Color.RED else Color.GREEN)
        }
    }

    private fun setupSpinners() {
        // Setup limit spinner
        val limitValues = arrayOf("100", "300", "500", "All")
        val limitAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, limitValues)
        limitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val limitSpinner = binding.timeframeSpinner
        val limitArrow = binding.limitSpinnerArrow
        limitSpinner.adapter = limitAdapter

        // Setup timeframe spinner
        val timeframeValues = arrayOf("1H", "24H", "7D", "30D")
        val timeframeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, timeframeValues)
        timeframeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val timeframeSpinner = binding.timeframeSpinner
        val timeframeArrow = binding.timeframeSpinnerArrow
        timeframeSpinner.adapter = timeframeAdapter

        // Handle spinner clicks
        limitSpinner.setOnTouchListener { _, _ ->
            limitArrow.animate().rotation(180f).setDuration(200).start()
            false
        }

        timeframeSpinner.setOnTouchListener { _, _ ->
            timeframeArrow.animate().rotation(180f).setDuration(200).start()
            false
        }

        // Reset arrows when selection is made
        limitSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                limitArrow.animate().rotation(0f).setDuration(200).start()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        timeframeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                timeframeArrow.animate().rotation(0f).setDuration(200).start()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 