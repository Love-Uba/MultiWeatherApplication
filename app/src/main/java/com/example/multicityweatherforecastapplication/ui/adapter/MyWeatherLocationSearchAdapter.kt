package com.example.multicityweatherforecastapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.databinding.FragmentWeatherLocationSearchListItemBinding

class MyWeatherLocationSearchAdapter : RecyclerView.Adapter<MyWeatherLocationSearchAdapter.ViewHolder>() {

    private var adapterList = emptyList<WeatherEntity>()
    private var onClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentWeatherLocationSearchListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(adapterList[position])
        val item = adapterList[position]
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, item)
        }
    }

    override fun getItemCount(): Int = adapterList.count()

    inner class ViewHolder(private val bnd: FragmentWeatherLocationSearchListItemBinding) :
        RecyclerView.ViewHolder(bnd.root) {

        fun bind(item: WeatherEntity) {
            item.run {
                bnd.weatherLocation.text = item.locationName
                bnd.weatherContent.text = item.country
            }
        }
    }

    fun setOnClickListener(listener: OnItemClickListener){
        this.onClickListener = listener
    }

    fun populateLocation(weatherEntity: List<WeatherEntity>) {
        this.adapterList = weatherEntity
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onClick(position: Int, model: WeatherEntity)
    }

}