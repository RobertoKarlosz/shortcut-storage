package com.myprojects.android.shortcutstorage.ui.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.myprojects.android.shortcutstorage.R
import com.myprojects.android.shortcutstorage.databinding.FragmentUpdateWorkBinding
import com.myprojects.android.shortcutstorage.model.storage.StorageItem
import com.myprojects.android.shortcutstorage.model.work.Work
import com.myprojects.android.shortcutstorage.ui.add_new.NewItemAdapter
import com.myprojects.android.shortcutstorage.ui.storage.StorageViewModel
import com.myprojects.android.shortcutstorage.ui.work.WorkViewModel

class UpdateWorkFragment : Fragment() {

    private lateinit var binding : FragmentUpdateWorkBinding
    private lateinit var adapter : NewItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateWorkBinding.inflate(inflater, container, false)

        val workViewModel : WorkViewModel = ViewModelProvider(requireActivity())[WorkViewModel::class.java]
        val storageViewModel : StorageViewModel = ViewModelProvider(requireActivity())[StorageViewModel::class.java]

        val autocompleteAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            storageViewModel.items.value?.map { it.name } ?: emptyList<String>()
        )

        binding.etUpdateWorkStorageItemName.setAdapter(autocompleteAdapter)

        val id : Int = workViewModel.updateWork.id
        val address : String = workViewModel.updateWork.address
        val date : String = workViewModel.updateWork.date
        val name : String = workViewModel.updateWork.name
        val description : String = workViewModel.updateWork.description
        val materials : ArrayList<StorageItem> = ArrayList(workViewModel.updateWork.materials)

        binding.etUpdateWorkAddress.setText(address)
        binding.etUpdateWorkDate.setText(date)
        binding.etUpdateWorkName.setText(name)
        binding.etUpdateWorkDescription.setText(description)

        adapter = NewItemAdapter(object : NewItemAdapter.OptionsMenuClickListener {
            override fun onOptionsMenuClicked(position: Int) {
                val itemToDelete = materials[position]
                materials.remove(itemToDelete)
                adapter.update(materials)
            }
        })

        binding.rvUpdateMaterials.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpdateMaterials.adapter = adapter
        adapter.update(materials)

        binding.btnUpdateWorkAddItem.setOnClickListener {
            onUpdateWorkAddItemClicked(materials)
        }

        binding.btnUpdateWork.setOnClickListener {
            onUpdateWorkClicked(workViewModel, id, materials)
        }

        return binding.root
    }

    private fun onUpdateWorkAddItemClicked(materials: ArrayList<StorageItem>) {
        if (validItem()) {
            val newItem = StorageItem(
                name = binding.etUpdateWorkStorageItemName.text.toString(),
                amount = binding.etUpdateWorkStorageItemAmount.text.toString().toInt(),
                unit = binding.etUpdateWorkStorageItemUnit.text.toString()
            )
            materials.add(newItem)
            adapter.update(materials)

            binding.etUpdateWorkStorageItemName.text.clear()
            binding.etUpdateWorkStorageItemAmount.text.clear()
            binding.etUpdateWorkStorageItemUnit.text.clear()
        } else {
            Snackbar.make(requireView(), "Fill all the items field", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun onUpdateWorkClicked(
        workViewModel: WorkViewModel,
        id: Int,
        materials: ArrayList<StorageItem>
    ) {
        if (validInput()) {
            workViewModel.updateWork(
                Work(
                    id = id,
                    name = binding.etUpdateWorkName.text.toString()
                        .ifEmpty { binding.etUpdateWorkAddress.text.toString() },
                    address = binding.etUpdateWorkAddress.text.toString(),
                    date = binding.etUpdateWorkDate.text.toString(),
                    description = binding.etUpdateWorkDescription.text.toString(),
                    materials = materials
                    //binding.etUpdateWorkMaterials.text.toList()
                )
            )
            Snackbar.make(requireView(), "Updated Successfully!", Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.fgUpdateWork)
                .show()
            findNavController().navigate(R.id.navigation_work)
        } else {
            if (binding.etUpdateWorkAddress.text.isEmpty()) {
                binding.etUpdateWorkAddress.error = "This field is required"
            }
            if (binding.etUpdateWorkDate.text.isEmpty()) {
                binding.etUpdateWorkDate.error = "This field is required"
            }
            Snackbar.make(requireView(), "Please fill all required field!", Snackbar.LENGTH_SHORT)
                .setAnchorView(binding.fgUpdateWork)
                .show()
        }
    }

    private fun validInput(): Boolean {
        return binding.etUpdateWorkAddress.text.isNotEmpty() &&
                binding.etUpdateWorkDate.text.isNotEmpty()
    }

    private fun validItem(): Boolean {
        return binding.etUpdateWorkStorageItemName.text.isNotEmpty() &&
                binding.etUpdateWorkStorageItemAmount.text.isNotEmpty() &&
                binding.etUpdateWorkStorageItemUnit.text.isNotEmpty()
    }
}