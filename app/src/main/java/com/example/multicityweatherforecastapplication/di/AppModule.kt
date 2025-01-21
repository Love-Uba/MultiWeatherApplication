package com.example.multicityweatherforecastapplication.di

import android.content.Context
import androidx.room.Room
import com.example.multicityweatherforecastapplication.data.ApiService
import com.example.multicityweatherforecastapplication.data.WeatherRepository
import com.example.multicityweatherforecastapplication.data.local.WeatherDao
import com.example.multicityweatherforecastapplication.data.local.WeatherDatabase
import com.example.multicityweatherforecastapplication.data.local.WeatherDatabaseRepository
import com.example.multicityweatherforecastapplication.data.local.WeatherDatabaseRepositoryImpl
import com.example.multicityweatherforecastapplication.data.wrapper.DataMapper
import com.example.multicityweatherforecastapplication.usecases.impls.GetForecastDataFromCoordinatesUseCaseImpl
import com.example.multicityweatherforecastapplication.usecases.impls.GetLocationFromQueryUseCaseImpl
import com.example.multicityweatherforecastapplication.usecases.impls.GetSavedWeatherUseCaseImpl
import com.example.multicityweatherforecastapplication.usecases.impls.GetWeatherDataFromCoordinatesUseCaseImpl
import com.example.multicityweatherforecastapplication.usecases.impls.LocationUseCaseImpl
import com.example.multicityweatherforecastapplication.usecases.impls.QueryInputValidation
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetForecastDataFromCoordinatesUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetLocationFromQueryUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetSavedWeatherUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.GetWeatherDataFromCoordinatesUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.InputValidationUseCase
import com.example.multicityweatherforecastapplication.usecases.interfaces.LocationUseCase
import com.example.multicityweatherforecastapplication.utils.UtilConstants.BASE_URL
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun makeGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideClient(
        logger: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addInterceptor(logger)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URL).build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase {
        var INSTANCE: WeatherDatabase? = null

        return INSTANCE ?: synchronized(this) {

            val instance = Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                WeatherDatabase.dbase
            ).build()

            INSTANCE = instance

            instance
        }
    }

    @Singleton
    @Provides
    fun provideWeatherDao(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.weatherDao()
    }

    @Provides
    fun provideWeatherDatabaseRepository(weatherDao: WeatherDao): WeatherDatabaseRepository =
        WeatherDatabaseRepositoryImpl(weatherDao)

    @Provides
    fun provideDataMapper(): DataMapper = DataMapper()

    @Provides
    fun provideRepository(apiService: ApiService, weatherDatabaseRepository: WeatherDatabaseRepository, dataMapper: DataMapper) = WeatherRepository(apiService, weatherDatabaseRepository, dataMapper)

    @Provides
    fun provideWeatherDataFromCoordinatesUseCase(repository: WeatherRepository): GetWeatherDataFromCoordinatesUseCase = GetWeatherDataFromCoordinatesUseCaseImpl(repository)

    @Provides
    fun provideForecastDataFromCoordinatesUseCase(repository: WeatherRepository): GetForecastDataFromCoordinatesUseCase = GetForecastDataFromCoordinatesUseCaseImpl(repository)

    @Provides
    fun provideGetLocationFromQueryUseCase(repository: WeatherRepository): GetLocationFromQueryUseCase = GetLocationFromQueryUseCaseImpl(repository)

    @Provides
    fun provideLocationUseCase(@ApplicationContext context: Context): LocationUseCase {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        return LocationUseCaseImpl(fusedLocationProviderClient)
    }

    @Provides
    fun provideQueryInputValidationUseCase(): InputValidationUseCase = QueryInputValidation()

    @Provides
    fun provideGetSavedWeatherUseCase(repository: WeatherRepository): GetSavedWeatherUseCase = GetSavedWeatherUseCaseImpl(repository)

}