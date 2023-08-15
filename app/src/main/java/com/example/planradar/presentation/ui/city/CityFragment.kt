package com.example.planradar.presentation.ui.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planradar.R
import com.example.planradar.databinding.FragmentCityBinding
import com.example.planradar.domain.model.City
import com.example.planradar.presentation.adapter.CityAdapter
import com.example.planradar.presentation.utils.removeDuplicates
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CityFragment : Fragment() {

    private val rootView: FragmentCityBinding by lazy {
        FragmentCityBinding.inflate(layoutInflater)
    }

    private val viewModel: CityViewModel by viewModel()

    private lateinit var cityAdapter: CityAdapter
    lateinit var cityList: ArrayList<City>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       // _binding = FragmentCityBinding.inflate(inflater, container, false)
        return rootView.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding.in.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
        cityAdapter = CityAdapter(requireContext(), object : CityAdapter.ActionClickListener {
            override fun showDetails(item: City) {
                rootView.indicator.visibility = View.INVISIBLE
                viewModel.onEvent(CityViewModel.Event.ShowDetails(item))
            }

            override fun showHistory(item: City) {
                Toast.makeText(requireContext(),item.name, Toast.LENGTH_SHORT).show()
            }

        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                cityAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(rootView.recyclerView)

        rootView.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = cityAdapter
        }

        cityList = ArrayList()
        cityList.add(City("London"))
        cityList.add(City("Paris"))
        cityList.add(City("Vienna"))
        cityList.add(City("Tehran"))

        cityAdapter.submitUpdate(cityList)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.cityViewState.collect(::renderStates)
            }
        }
        rootView.addCity.setOnClickListener {
            viewModel.onEvent(CityViewModel.Event.AddCity(getRandomCity()))
        }
    }

    private fun renderStates(state: CityViewModel.CityState) {
        when (state) {
            is CityViewModel.CityState.CityAdded -> {
                cityList.add(City(state.city))
                cityAdapter.submitUpdate(cityList.removeDuplicates())
            }
            is CityViewModel.CityState.DetailsLoaded -> {
                state.item.name?.let { loadDetailFragment(R.id.DetailsFragment, it) }
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

    private fun getRandomCity(): String {
        val cities = listOf("New York City", "Tehran", "Ahvaz", "Tokyo", "Sydney")
        val random = Random(System.currentTimeMillis())
        val index = random.nextInt(cities.size)
        return cities[index]
    }

    companion object {
        const val CITY_NAME_KEY = "city"
    }
}