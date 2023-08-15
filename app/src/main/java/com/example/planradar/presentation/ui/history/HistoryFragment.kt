package com.example.planradar.presentation.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.planradar.R
import com.example.planradar.databinding.FragmentHistoryBinding
import com.example.planradar.domain.model.WeatherResponse
import com.example.planradar.presentation.adapter.HistoryAdapter
import com.example.planradar.presentation.ui.city.CityFragment.Companion.CITY_NAME_KEY
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {

    private val rootView: FragmentHistoryBinding by lazy {
        FragmentHistoryBinding.inflate(layoutInflater)
    }

    private val viewModel: HistoryViewModel by viewModel()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return rootView.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        historyAdapter = HistoryAdapter(requireContext(), object : HistoryAdapter.ActionClickListener{
            override fun showDetails(item: WeatherResponse) {
                viewModel.onEvent(HistoryViewModel.Event.ShowDetails(item))
            }

        })
        rootView.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = historyAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.historyViewState.collect(::renderStates)
            }
        }

        val city = arguments?.getString(CITY_NAME_KEY)
        city?.let {
            viewModel.getAllHistory(city)
        }?: Log.e("HistoryFragment","The city name is empty.")

        rootView.title.text =
            getString(R.string.concat_strings, city, getString(R.string.historical))

        rootView.done.text = getText(R.string.done)
        rootView.done.setOnClickListener { getNavController().navigateUp() }
    }

    private fun renderStates(state: HistoryViewModel.HistoryState) {
        when (state) {
            is HistoryViewModel.HistoryState.Loading -> {}
            is HistoryViewModel.HistoryState.ShowHistory -> {
                historyAdapter.submitUpdate(state.item)
            }
            is HistoryViewModel.HistoryState.ShowDetails -> {
                loadDetailFragment(R.id.DetailsFragment, state.item.name)
            }
            is HistoryViewModel.HistoryState.ShowHistoryFailed -> {
                state.error
            }
        }

    }
    private fun getNavController(): NavController {
        return requireView().findNavController()
    }

    private fun loadDetailFragment(@IdRes viewId: Int, city: String) {
        val bundle = Bundle()
        bundle.putString(CITY_NAME_KEY, city)
        getNavController().navigate(viewId, bundle)
    }
}