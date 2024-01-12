package com.myprojects.android.shortcutstorage.ui.add_new

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.myprojects.android.shortcutstorage.R
import com.myprojects.android.shortcutstorage.databinding.FragmentAddStorageItemBinding
import com.myprojects.android.shortcutstorage.model.storage.StorageItem
import com.myprojects.android.shortcutstorage.ui.storage.StorageViewModel

class AddStorageItemFragment : Fragment() {
    private lateinit var binding : FragmentAddStorageItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddStorageItemBinding.inflate(inflater, container, false)

        val viewModel : StorageViewModel = ViewModelProvider(requireActivity())[StorageViewModel::class.java]

        val storageitemsNames = viewModel.items.value?.map { it.name } ?: emptyList()

        binding.etAddStorageItemName.setAdapter( ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                storageitemsNames
            )
        )

        binding.btnAddStorageItem.setOnClickListener {
            onAddStorageItemClicked(storageitemsNames, viewModel)
        }


        return binding.root
    }

    private fun onAddStorageItemClicked(
        storageitemsNames: List<String>,
        viewModel: StorageViewModel
    ) {
        if (validInput()) {
            if (storageitemsNames.contains(binding.etAddStorageItemName.text.toString())) {
                val updateItem =
                    viewModel.items.value!![storageitemsNames.indexOf(binding.etAddStorageItemName.text.toString())]
                updateItem.amount += binding.etAddStorageItemAmount.text.toString().toInt()
                viewModel.updateItem(updateItem)
            } else {
                viewModel.add(
                    StorageItem(
                        id = 0,
                        name = binding.etAddStorageItemName.text.toString(),
                        amount = binding.etAddStorageItemAmount.text.toString().toInt(),
                        unit = binding.etAddStorageItemUnit.text.toString()
                    )
                )
            }
            Snackbar.make(requireView(), "Added Successfully!", Snackbar.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.navigation_storage)
        } else {
            Snackbar.make(requireView(), "Please fill all field!", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun validInput(): Boolean {
        return binding.etAddStorageItemName.text.isNotEmpty() &&
                binding.etAddStorageItemAmount.text.isNotEmpty() &&
                binding.etAddStorageItemUnit.text.isNotEmpty()
    }

}
