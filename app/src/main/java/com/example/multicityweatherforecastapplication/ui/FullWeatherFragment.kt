package com.example.multicityweatherforecastapplication.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.multicityweatherforecastapplication.R
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.databinding.FragmentFullWeatherBinding
import com.example.multicityweatherforecastapplication.ui.adapter.ForecastDaysAdapter
import com.example.multicityweatherforecastapplication.utils.showcurrentTime
import com.example.multicityweatherforecastapplication.utils.toString
import com.example.multicityweatherforecastapplication.viewmodels.FullWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullWeatherFragment : Fragment() {

    private lateinit var bnd: FragmentFullWeatherBinding
    private val viewModel: FullWeatherViewModel by viewModels()
    private val forecastAdapter = ForecastDaysAdapter()
    private var favedWeather: WeatherEntity? = null
    private var favedStatus: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bnd = FragmentFullWeatherBinding.inflate(layoutInflater, container, false)
        val longLatData = FullWeatherFragmentArgs.fromBundle(requireArguments()).longlatdata
        longLatData?.let {
            viewModel.updateLatitudeAndLongitude(
                it
            )
        }
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchLocation()
        setUpViews()
        setUpViewModels()
    }

    private fun fetchLocation() {
        viewModel.startLocationUpdates(
            onLocationReceived = { lat, lon ->
                viewModel.actionGetLocationWeather(lat, lon)
                viewModel.actionGetLocationForecast(lat, lon)
            },
            onError = { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun setUpViews() {
        bnd.dateTv.text = currentDate
        bnd.forecastList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = forecastAdapter
            setHasFixedSize(true)
        }

        bnd.searchDrawer.setOnClickListener {
            findNavController().navigate(FullWeatherFragmentDirections.actionFullWeatherFragmentToWeatherLocationSearchFragment())
        }

        bnd.favesDrawer.setOnClickListener {
            findNavController().navigate(FullWeatherFragmentDirections.actionFullWeatherFragmentToFavoritesLocationsFragment())
        }

        bnd.saveFaveBtn.setOnClickListener {
            Toast.makeText(activity, "Saved", Toast.LENGTH_LONG).show()
            favedWeather?.let { fave ->
                viewModel.saveFaveWeather(
                    fave,
                    !fave.saved
                )
            }
        }
    }

    private val currentDate = showcurrentTime().toString("dd/MM/yyyy")

    private fun setUpViewModels() {
        viewModel.triggerRequestForPermission = { startLocationPermissionRequest() }
        viewModel.weatherDataResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed to Fetch Weather Data",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is Result.Loading -> {}

                is Result.Success -> {
                    viewModel.stopLocationUpdates()
                    favedWeather = result.value
                    favedStatus = result.value.saved
                    bnd.cityNameTv.text = result.value.locationName
                    bnd.tempTv.text =
                        getString(R.string.temp, result.value.temp.toString())
                    bnd.humidityPercentTv.text = result.value.humidity.toString()
                    bnd.windSpeedTv.text = result.value.wind.toString()
                    bnd.feelsLikeTempTv.text = result.value.feelsLike.toString()
                    bnd.weatherTv.text = result.value.status
                    bnd.weatherLowTv.text =
                        getString(R.string.low_temp, result.value.tempMin.toString())
                    bnd.weatherHighTv.text =
                        getString(R.string.high_temp, result.value.tempMax.toString())
                    val icon = result.value.icon
                    Glide.with(requireContext())
                        .load("https://openweathermap.org/img/wn/$icon@2x.png")
                        .into(bnd.weatherIcon)
                }
            }
        }

        viewModel.forecastDataResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed to Fetch Weather Data",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is Result.Loading -> {
                    bnd.loadingView.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    bnd.loadingView.visibility = View.GONE
                    bnd.forecastList.visibility = View.VISIBLE
                    forecastAdapter.populatePredictions(result.value)
                }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.triggerLocationFetch()
        }
    }

    private fun startLocationPermissionRequest() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        fetchLocation()
    }
}