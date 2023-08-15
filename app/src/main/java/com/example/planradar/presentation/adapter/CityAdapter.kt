package com.example.planradar.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.planradar.R
import com.example.planradar.databinding.ItemCityBinding
import com.example.planradar.domain.model.City

class CityAdapter(
    private val context: Context,
    private val listener: ActionClickListener
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private val cities: ArrayList<City> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val views = ItemCityBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(views)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cities[position]
        holder.itemView.setOnClickListener {
            listener.showDetails(item)
        }
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    fun submitUpdate(update: List<City>) {
        val callBack = CityDiffCallback(cities, update)
        val diffResult = DiffUtil.calculateDiff(callBack)
        cities.clear()
        cities.addAll(update)
        diffResult.dispatchUpdatesTo(this)
    }

    fun deleteItem(position: Int) {
        cities.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(private val views: ItemCityBinding) :
        RecyclerView.ViewHolder(views.root) {
        fun bind(position: Int) = views.apply {
            cities[position].also { city ->
                city.name?.let {
                    nameOfCity.text = it
                    Glide.with(context)
                        .load(R.drawable.baseline_info_24)
                        .error(R.drawable.ic_launcher_background)
                        .into(info)

                    info.setOnClickListener { listener.showHistory(city) }
                }
            }
        }
    }

    class CityDiffCallback(
        private val oldCities: List<City>,
        private val newCities: List<City>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldCities.size
        }

        override fun getNewListSize(): Int {
            return newCities.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCities[oldItemPosition] == newCities[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCities[oldItemPosition].name == newCities[newItemPosition].name
        }

    }

    interface ActionClickListener {
        fun showDetails(item:City)
        fun showHistory(item:City)
    }
}