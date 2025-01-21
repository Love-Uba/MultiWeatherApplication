package com.example.multicityweatherforecastapplication.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.multicityweatherforecastapplication.data.local.WeatherEntity
import com.example.multicityweatherforecastapplication.data.models.LongLatData
import com.example.multicityweatherforecastapplication.data.models.searchlocationmodel.SearchLocationDataItem
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.databinding.FragmentWeatherLocationSearchBinding
import com.example.multicityweatherforecastapplication.ui.adapter.MyWeatherLocationSearchAdapter
import com.example.multicityweatherforecastapplication.utils.hide
import com.example.multicityweatherforecastapplication.utils.show
import com.example.multicityweatherforecastapplication.viewmodels.SearchLocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherLocationSearchFragment : Fragment() {

    private lateinit var bnd: FragmentWeatherLocationSearchBinding
    private val viewModel: SearchLocationViewModel by viewModels()
    private val locationAdapter = MyWeatherLocationSearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentWeatherLocationSearchBinding.inflate(layoutInflater, container, false)
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleSearchInput()
        setUpViewModel()
        setUpViews()
    }

    private fun setUpViews() {
        bnd.searchResultRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationAdapter
            setHasFixedSize(true)
        }

        locationAdapter.setOnClickListener(
            object : MyWeatherLocationSearchAdapter.OnItemClickListener{
                override fun onClick(position: Int, model: WeatherEntity) {
                    val action = WeatherLocationSearchFragmentDirections.actionWeatherLocationSearchFragmentToFullWeatherFragment(
                        LongLatData(model.longitude, model.latitude)
                    )
                    findNavController().navigate(action)
                }
            }
        )
    }

    private fun handleSearchInput() {
        bnd.locationSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                input: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun afterTextChanged(input: Editable?) {}
            override fun onTextChanged(input: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.updateSearchQuery(input.toString())
            }
        })
    }

    private fun setUpViewModel() {
        viewModel.searchLocationDataResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed to Find Location Data",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is Result.Loading -> {
                    Toast.makeText(activity, "Loading Weather Data...", Toast.LENGTH_LONG).show()
                    bnd.searchLoadingView.show()
                }

                is Result.Success -> {
                    bnd.searchLoadingView.hide()
                    locationAdapter.populateLocation(
                        result.value.map { locationData ->
                            WeatherEntity(
                                "",
                                locationData.name,
                                locationData.country,
                                locationData.lon,
                                locationData.lat,
                                0,
                                "",
                                "",
                                0.0,
                                0,
                                0.0,
                                0.0,
                                0.0,
                                0.0,
                                false
                            )
                        }
                    )
                    bnd.searchResultRv.show()
                }

            }
        }
    }

}