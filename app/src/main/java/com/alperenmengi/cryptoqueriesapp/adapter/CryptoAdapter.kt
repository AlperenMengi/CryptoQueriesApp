package com.alperenmengi.cryptoqueriesapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alperenmengi.cryptoqueriesapp.databinding.ItemCryptoBinding
import com.alperenmengi.cryptoqueries.model.CryptoData
import com.alperenmengi.cryptoqueriesapp.util.DateFormatter

class CryptoAdapter : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {
    private var cryptoList: List<CryptoData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val binding = ItemCryptoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CryptoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        holder.bind(cryptoList[position])
    }

    override fun getItemCount() = cryptoList.size

    class CryptoViewHolder(private val binding: ItemCryptoBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(crypto: CryptoData) {
            with(binding) {
                nameText.text = crypto.name
                priceText.text = crypto.price
                mcapText.text = crypto.marketCap
                fdvRateText.text = crypto.fdvRate
                changeText.text = crypto.change24h
                athText.text = "${crypto.ath.price}\n(${DateFormatter.formatDate(crypto.ath.date)})"

                // Set text color for change percentage
                /*changeText.setTextColor(if (crypto.change24h.startsWith("-"))
                    Color.RED else Color.GREEN)*/
            }
        }
    }

    fun updateList(newList: List<CryptoData>) {
        cryptoList = newList
        notifyDataSetChanged()
    }
} 