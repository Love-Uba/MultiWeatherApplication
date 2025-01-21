package com.example.multicityweatherforecastapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.databinding.ForecastDaysItemBinding
import com.example.multicityweatherforecastapplication.utils.UtilConstants.DATE_DISPLAY_FORMAT
import com.example.multicityweatherforecastapplication.utils.convertLongToTime
import com.example.multicityweatherforecastapplication.utils.getFormattedDateTime

class ForecastDaysAdapter : RecyclerView.Adapter<ForecastDaysAdapter.BaseViewHolder>() {

    var adapterList = emptyList<WeatherEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val bnd = ForecastDaysItemBinding.inflate(layoutInflater, parent, false)
        return BaseViewHolder(bnd)
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(adapterList[position])
    }

    override fun getItemCount(): Int {
        return adapterList.count()
    }

    inner class BaseViewHolder(private val bnd: ForecastDaysItemBinding) :
        RecyclerView.ViewHolder(bnd.root) {
        fun bind(item: WeatherEntity) {

            item.run {
                bnd.weatherSummaryTv.text = item.status
                val icon = item.icon
                Glide.with(itemView.context)
                    .load("https://openweathermap.org/img/wn/$icon@2x.png")
                    .into(bnd.weatherIv)
                bnd.tempRangeTv.text = "${item.tempMin}/${item.tempMax}"
                bnd.daysTv.text = convertLongToTime(item.date.toLong())
            }

        }
    }

    fun populatePredictions(weatherEntity: List<WeatherEntity>) {
        this.adapterList = weatherEntity
        notifyDataSetChanged()
    }
}