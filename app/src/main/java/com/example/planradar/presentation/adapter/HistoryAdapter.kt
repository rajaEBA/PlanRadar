package com.example.planradar.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.planradar.databinding.ItemHistoryBinding
import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.presentation.utils.kelvinToCelsius

class HistoryAdapter(
    private val context: Context,
    private val listener: ActionClickListener
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val histories: ArrayList<WeatherResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val views = ItemHistoryBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(views)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val item = histories[position]
        holder.itemView.setOnClickListener {
            listener.showDetails(item)
        }
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    fun submitUpdate(update: List<WeatherResponse>) {
        val callBack = AssetDiffCallback(histories, update)
        val diffResult = DiffUtil.calculateDiff(callBack)
        histories.clear()
        histories.addAll(update)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val views: ItemHistoryBinding) :
        RecyclerView.ViewHolder(views.root) {
        fun bind(position: Int) = views.apply {
            histories[position].also { weather ->
                weather.let {
                    time.text = weather.timezone
                    description.text = weather.description + ", " + weather.temp.kelvinToCelsius() + "C"

                    info.setOnClickListener { listener.showDetails(weather) }
                }
            }
        }
    }

    class AssetDiffCallback(
        private val oldAssets: List<WeatherResponse>,
        private val newAssets: List<WeatherResponse>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldAssets.size
        }

        override fun getNewListSize(): Int {
            return newAssets.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldAssets[oldItemPosition] == newAssets[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldAssets[oldItemPosition].timezone == newAssets[newItemPosition].timezone
        }

    }

    interface ActionClickListener {
        fun showDetails(item: WeatherResponse)
    }
}