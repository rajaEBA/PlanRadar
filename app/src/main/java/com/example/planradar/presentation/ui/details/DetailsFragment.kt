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
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.assetsViewState.collect(::renderStates)
            }
        }
        val city = arguments?.getString(CityFragment.CITY_NAME_KEY)
        city?.let {

            viewModel.getWeather(city, getString(R.string.api_key))
        } ?: Log.e("DetailsFragment", "There is no value for city")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderStates(state: DetailsViewModel.DetailsState) {
        when (state) {
            is DetailsViewModel.DetailsState.Loading -> {
                binding.indicator.visibility = View.VISIBLE
            }
            is DetailsViewModel.DetailsState.ShowWeather -> {
                binding.indicator.visibility = View.INVISIBLE
                Log.i("DetailsFragment----",state.item.toString())
                showDetails(state.item)
            }
            is DetailsViewModel.DetailsState.ShowWeatherFailed -> {
                binding.indicator.visibility = View.INVISIBLE
                state.error
            }
        }
    }

    private fun showDetails(item: WeatherResponse) {
        val url = BASE_IMAGE_URL + item.weather[0].icon + ".png"
        Log.i("URL-----", url)
        binding.apply {
            nameOfCity.text = item.name
            time.text = getString(R.string.time_label, item.name, getTime(item.timezone))
            description.text = getString(R.string.description_label, item.weather[0].description)
            temp.text = getString(R.string.temp_label, kelvinToCelsius(item.main.temp).toString())
            humidity.text = getString(R.string.humidity_label, item.main.humidity.toString() + "%")
            windspeed.text = getString(R.string.windspeed_label, item.wind.speed.toString())
            Glide.with(requireContext())
                .load(url)
                .into(weatherImage)

                //.error(R.drawable.ic_launcher_background)

        }
        binding.back.setOnClickListener { getNavController().navigateUp() }

    }

    private fun getTime(timezoneOffset: Int): String {
        val currentTime = Instant.now().plusSeconds(timezoneOffset.toLong())

        // Convert the instant to a human-readable format
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return formatter.format(currentTime.atZone(ZoneId.systemDefault()))

    }

   private  fun kelvinToCelsius(kelvinTemperature: Double): Int {
        return (kelvinTemperature - 273.15).toInt()
    }

    private fun getNavController(): NavController {
        return requireView().findNavController()
    }

    companion object{
        private const val BASE_IMAGE_URL = "http://openweathermap.org/img/w/"
    }
}