package com.example.multicityweatherforecastapplication.ui

import android.os.Bundle
import android.util.Log
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
import com.example.multicityweatherforecastapplication.data.wrapper.Result
import com.example.multicityweatherforecastapplication.databinding.FragmentFavoritesLocationsBinding
import com.example.multicityweatherforecastapplication.ui.adapter.MyWeatherLocationSearchAdapter
import com.example.multicityweatherforecastapplication.utils.show
import com.example.multicityweatherforecastapplication.viewmodels.SaveFavouriteWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesLocationsFragment : Fragment() {

    private lateinit var bnd: FragmentFavoritesLocationsBinding
    private val favouriteAdapter = MyWeatherLocationSearchAdapter()
    private val viewModel: SaveFavouriteWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bnd = FragmentFavoritesLocationsBinding.inflate(layoutInflater, container, false)
        return bnd.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchSavedWeather()
    }

    private fun setUpViews() {
        bnd.savedResultRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favouriteAdapter
            setHasFixedSize(true)
        }

        favouriteAdapter.setOnClickListener(
            object : MyWeatherLocationSearchAdapter.OnItemClickListener{
                override fun onClick(position: Int, model: WeatherEntity) {
                    val action = FavoritesLocationsFragmentDirections.actionFavoritesLocationsFragmentToFullWeatherFragment(
                        LongLatData(model.longitude, model.latitude)
                    )
                    findNavController().navigate(action)
                }
            }
        )
    }

    private fun setUpViewModel() {
        viewModel.savedFaveWeatherDataResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed to Fetch Weather Data",
                        Toast.LENGTH_LONG
                    ).show()
                }

                is Result.Loading -> {
                }

                is Result.Success -> {
                    bnd.savedResultRv.show()
                    favouriteAdapter.populateLocation(result.value)
                }
            }


        }
    }
}
