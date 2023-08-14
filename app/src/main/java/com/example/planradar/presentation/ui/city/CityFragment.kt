package com.example.planradar.presentation.ui.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.planradar.databinding.FragmentCityBinding
import com.example.planradar.domain.model.City
import com.example.planradar.presentation.adapter.CityAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CityFragment : Fragment() {

    private var _binding: FragmentCityBinding? = null
    private val binding get() = _binding!!

    private lateinit var cityAdapter: CityAdapter
    lateinit var cityList: ArrayList<City>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCityBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.in.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
        cityAdapter = CityAdapter(requireContext(), object : CityAdapter.ActionClickListener {
            override fun showDetails(item: City) {
                binding.indicator.visibility = View.INVISIBLE
                //loadDetailFragment(R.id.assetDetailsFragment, item)
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
                val deleteCity = cityList[viewHolder.adapterPosition]
                cityAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recyclerView)

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = cityAdapter
        }

        cityList = ArrayList()
        cityList.add(City("London"))
        cityList.add(City("Paris"))
        cityList.add(City("Vienna"))
        cityList.add(City("Tehran"))


        cityAdapter.submitUpdate(cityList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}