package com.example.planradar.presentation.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.planradar.R
import com.example.planradar.databinding.FragmentDetailsBinding
import com.example.planradar.domain.model.WeatherResponse
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
        viewModel.getWeather("London", getString(R.string.api_key))

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderStates(state: DetailsViewModel.DetailsState) {
        when (state) {
            is DetailsViewModel.DetailsState.Loading -> {

            }
            is DetailsViewModel.DetailsState.ShowWeather -> {
                showDetails(state.item)
            }
            is DetailsViewModel.DetailsState.ShowWeatherFailed -> {
                state.error
            }
        }
    }

    private fun showDetails(item: WeatherResponse) {
//        binding.apply {
//            nameOfCity.text = item.name
//            time.text = getTime(item.timezone)
//            description.text = item.weather[0].description
//            temp.text = item.main.temp.toString()
//            humidity.text = item.main.humidity.toString()
//            windspeed.text = item.wind.speed.toString()
//
//        }

    }

    private fun getTime(timezoneOffset: Int): String {
        val currentTime = Instant.now().plusSeconds(timezoneOffset.toLong())

        // Convert the instant to a human-readable format
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return formatter.format(currentTime.atZone(ZoneId.systemDefault()))

    }
}