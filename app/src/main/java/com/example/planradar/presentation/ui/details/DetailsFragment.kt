package com.example.planradar.presentation.ui.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.planradar.R
import com.example.planradar.databinding.FragmentDetailsBinding
import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.presentation.ui.city.CityFragment
import com.example.planradar.presentation.utils.kelvinToCelsius
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : Fragment() {

    private val rootView: FragmentDetailsBinding by lazy {
        FragmentDetailsBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return rootView.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.detailsViewState.collect(::renderStates)
            }
        }
        val city = arguments?.getString(CityFragment.CITY_NAME_KEY)
        city?.let {

            viewModel.getWeather(city)
        } ?: Log.e("DetailsFragment", "There is no value for city")
    }

    private fun renderStates(state: DetailsViewModel.DetailsState) {
        when (state) {
            is DetailsViewModel.DetailsState.Loading -> {
                rootView.indicator.visibility = View.VISIBLE
            }
            is DetailsViewModel.DetailsState.ShowWeather -> {
                rootView.indicator.visibility = View.INVISIBLE
                showDetails(state.item)
            }
            is DetailsViewModel.DetailsState.ShowWeatherFailed -> {
                rootView.indicator.visibility = View.INVISIBLE
                state.error
            }
        }
    }

    private fun showDetails(item: WeatherResponse) {
        val url = BASE_IMAGE_URL + item.icon + ".png"
        rootView.apply {
            nameOfCity.text = item.name
            time.text = getString(R.string.time_label, item.name, item.timezone)
            description.text = getString(R.string.description_label, item.description)
            temp.text = getString(R.string.temp_label, item.temp.kelvinToCelsius().toString())
            humidity.text = getString(R.string.humidity_label, item.humidity.toString() + "%")
            windspeed.text = getString(R.string.windspeed_label, item.windSpeed.toString())
            Glide.with(requireContext())
                .load(url)
                .into(weatherImage)
        }
        rootView.back.setOnClickListener { getNavController().navigateUp() }

    }

    private fun getNavController(): NavController {
        return requireView().findNavController()
    }

    companion object{
        private const val BASE_IMAGE_URL = "http://openweathermap.org/img/w/"
    }
}