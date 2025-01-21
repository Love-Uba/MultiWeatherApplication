package com.example.multicityweatherforecastapplication.data.local

class WeatherDatabaseRepositoryImpl(
    private val weatherDao: WeatherDao
) : WeatherDatabaseRepository {

    override suspend fun fetchSavedFavoriteWeatherEntityFromRepo() = weatherDao.fetchSavedWeather()

    override suspend fun fetchWeatherEntity(lat: Double, longitude: Double) = weatherDao.fetchWeather(lat, longitude)

    override suspend fun addWeatherEntityToRepo(weatherEntity: WeatherEntity) = weatherDao.insertWeather(weatherEntity)

    override suspend fun addForecastDataToRepo(forecast: List<WeatherEntity>) = weatherDao.insertForecast(forecast)

    override suspend fun fetchForecastWeather(
        lat: Double,
        longitude: Double
    ) = weatherDao.fetchForecastWeather(lat, longitude)

    override suspend fun updateFavoriteWeatherDataEntitySaveStatus(weatherEntity: WeatherEntity) = weatherDao.saveFaveWeatherData(weatherEntity)

    override suspend fun clearAllDataFromDatabase() = weatherDao.clearAll()
}