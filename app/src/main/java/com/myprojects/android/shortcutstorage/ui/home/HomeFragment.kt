package com.myprojects.android.shortcutstorage.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myprojects.android.shortcutstorage.databinding.FragmentHomeBinding
import com.myprojects.android.shortcutstorage.model.storage.StorageItem
import com.myprojects.android.shortcutstorage.model.work.Work
import com.myprojects.android.shortcutstorage.ui.storage.StorageViewModel
import com.myprojects.android.shortcutstorage.ui.work.WorkAdapter
import com.myprojects.android.shortcutstorage.ui.work.WorkAdapter.WorkButtonClickListener
import com.myprojects.android.shortcutstorage.ui.work.WorkMaterialsAdapter
import com.myprojects.android.shortcutstorage.ui.work.WorkViewModel

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var storageViewModel: StorageViewModel
    private lateinit var workViewModel: WorkViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        storageViewModel = ViewModelProvider(requireActivity())[StorageViewModel::class.java]
        workViewModel = ViewModelProvider(requireActivity())[WorkViewModel::class.java]

        val topWorkAdapter = WorkAdapter(object : WorkButtonClickListener {
            override fun onOptionsMenuClicked(position: Int) {}

            override fun onDoneClicked(position: Int) {}
        })

        workViewModel.activeWorks.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                topWorkAdapter.update(it)
            }
            else {
                topWorkAdapter.update(listOf(
                    Work(
                        id = 0,
                        address = "",
                        date = "",
                        name = "No active work",
                        description = "",
                        materials = emptyList(),
                        done = false
                    )).toList()
                )
            }
        }

        var storageItems = ArrayList<StorageItem>()
        var materials = ArrayList<StorageItem>()
        val materialsAdapter = WorkMaterialsAdapter(sumNeedToBuyMaterials(materials, storageItems))

        workViewModel.activeWorks.observe(viewLifecycleOwner) {
            materials = sumAllWorkMaterials(it)
            materialsAdapter.update(sumNeedToBuyMaterials(materials, storageItems))
        }

        storageViewModel.items.observe(viewLifecycleOwner) {
            storageItems = it as ArrayList<StorageItem>
            materialsAdapter.update(sumNeedToBuyMaterials(materials, storageItems))
        }

        binding.rvTopWork.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTopWork.adapter = topWorkAdapter

        binding.rvNeedToBy.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNeedToBy.adapter = materialsAdapter

        return binding.root
    }

    private fun sumAllWorkMaterials(works: List<Work>): ArrayList<StorageItem> {
        val sumMaterials = ArrayList<StorageItem>()
        for (work in works) {
            for (item in work.materials) {
                val sumMaterialsNames = sumMaterials.map { it.name }
                if (sumMaterialsNames.contains(item.name)) {
                    val index = sumMaterialsNames.indexOf(item.name)
                    sumMaterials[index].amount += item.amount
                } else {
                    sumMaterials.add(
                        StorageItem(
                            id = item.id,
                            name = item.name,
                            amount = item.amount,
                            unit = item.unit
                        )
                    )
                }
            }
        }
        return sumMaterials
    }

    private fun sumNeedToBuyMaterials(sumMaterials: List<StorageItem>, storageItems: List<StorageItem>): List<StorageItem> {
        val needMaterials = ArrayList<StorageItem>()
        for(item in sumMaterials) {
            val sumMaterialsNames = sumMaterials.map { it.name }
            val storageItemNames = storageItems.map { it.name }
            if (storageItemNames.contains(item.name)) {
                val storageIndex = storageItemNames.indexOf(item.name)
                val materialIndex = sumMaterialsNames.indexOf(item.name)
                sumMaterials[materialIndex].amount -= storageItems[storageIndex].amount
            }
            if(item.amount > 0) {
                needMaterials.add(item)
            }
        }
        return needMaterials
    }
}