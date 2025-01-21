package com.example.multicityweatherforecastapplication.data.wrapper

import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.models.baseweathermodel.FullWeatherResponse
import com.example.multicityweatherforecastapplication.data.models.forecastmodel.City
import com.example.multicityweatherforecastapplication.data.models.forecastmodel.ForecastWeatherResponse
import com.example.multicityweatherforecastapplication.data.models.forecastmodel.FullForecastResponse

class DataMapper {

    fun weatherMapper(weatherResponse: FullWeatherResponse): WeatherEntity {
        return WeatherEntity(
            weatherResponse.name+weatherResponse.dt,
            weatherResponse.name,
            weatherResponse.sys.country,
            weatherResponse.coord.lon,
            weatherResponse.coord.lat,
            weatherResponse.dt,
            weatherResponse.weather[0].main,
            weatherResponse.weather[0].icon,
            weatherResponse.main.temp,
            weatherResponse.main.humidity,
            weatherResponse.wind.speed,
            weatherResponse.main.feels_like,
            weatherResponse.main.temp_min,
            weatherResponse.main.temp_max,
            false
        )
    }

    fun forecastMapper(forecastLocationData: City, forecastResponse: ForecastWeatherResponse): WeatherEntity {
        return WeatherEntity(
            forecastLocationData.name+forecastResponse.dt,
            forecastLocationData.name,
            forecastLocationData.country,
            forecastLocationData.coord.lon,
            forecastLocationData.coord.lat,
            forecastResponse.dt,
            forecastResponse.weather[0].main,
            forecastResponse.weather[0].icon,
            forecastResponse.main.temp,
            forecastResponse.main.humidity,
            forecastResponse.wind.speed,
            forecastResponse.main.feels_like,
            forecastResponse.main.temp_min,
            forecastResponse.main.temp_max,
            false
        )
    }
}
