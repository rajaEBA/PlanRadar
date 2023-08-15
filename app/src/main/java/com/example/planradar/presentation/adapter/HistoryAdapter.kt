package com.example.planradar.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.planradar.R
import com.example.planradar.databinding.ItemCityBinding
import com.example.planradar.databinding.ItemHistoryBinding
import com.example.planradar.domain.model.City
import com.example.planradar.domain.model.History

class HistoryAdapter(
    private val context: Context,
    private val listener: CityAdapter.ActionClickListener
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private val histories: ArrayList<History> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAdapter.ViewHolder {
        val views = ItemHistoryBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(views)
    }

    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {
        val item = histories[position]
        holder.itemView.setOnClickListener {
            listener.showDetails(item)
        }
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return histories.size
    }


    inner class ViewHolder(private val views: ItemHistoryBinding) :
        RecyclerView.ViewHolder(views.root) {
        fun bind(position: Int) = views.apply {
            histories[position].also { history ->
                city.name?.let {
                    nameOfCity.text = it
                    Glide.with(context)
                        .load(R.drawable.baseline_info_24)
                        .error(R.drawable.ic_launcher_background)
                        .into(info)

                    info.setOnClickListener { listener.showHistory(history) }
                }
            }
        }
    }

    class AssetDiffCallback(
        private val oldAssets: List<City>,
        private val newAssets: List<City>
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
            return oldAssets[oldItemPosition].name == newAssets[newItemPosition].name
        }

    }

    interface ActionClickListener {
        fun showDetails(item: History)
    }
}