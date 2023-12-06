package com.myprojects.android.shortcutstorage.ui.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.myprojects.android.shortcutstorage.R
import com.myprojects.android.shortcutstorage.databinding.FragmentUpdateStorageItemBinding
import com.myprojects.android.shortcutstorage.model.storage.StorageItem
import com.myprojects.android.shortcutstorage.ui.storage.StorageViewModel

class UpdateStorageItemFragment : Fragment() {

    private lateinit var binding : FragmentUpdateStorageItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateStorageItemBinding.inflate(inflater, container, false)

        val viewModel = ViewModelProvider(requireActivity())[StorageViewModel::class.java]

        val storageitemsNames = viewModel.items.value?.map { it.name } ?: emptyList()

        val id : Int = viewModel.updateItem.id
        val name : String = viewModel.updateItem.name
        val amount : Int = viewModel.updateItem.amount
        val unit : String = viewModel.updateItem.unit

        binding.etUpdateStorageItemName.setText(name)
        binding.etUpdateStorageItemAmount.setText(amount.toString())
        binding.etUpdateStorageItemUnit.setText(unit)

        binding.btnUpdate.setOnClickListener {
            onUpdateClicked(storageitemsNames, viewModel, id)
        }
        return binding.root
    }

    private fun onUpdateClicked(
        storageitemsNames: List<String>,
        viewModel: StorageViewModel,
        id: Int
    ) {
        if (validInput()) {
            if (storageitemsNames.contains(binding.etUpdateStorageItemName.text.toString())) {
                val updateItem =
                    viewModel.items.value!![storageitemsNames.indexOf(binding.etUpdateStorageItemName.text.toString())]
                updateItem.amount = binding.etUpdateStorageItemAmount.text.toString().toInt()
                viewModel.updateItem(updateItem)
            } else {
                viewModel.updateItem(
                    StorageItem(
                        id = id,
                        name = binding.etUpdateStorageItemName.text.toString(),
                        amount = binding.etUpdateStorageItemAmount.text.toString().toInt(),
                        unit = binding.etUpdateStorageItemUnit.text.toString()
                    )
                )
            }
            Snackbar.make(requireView(), "Updated Successfully!", Snackbar.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.navigation_storage)
        } else {
            Snackbar.make(requireView(), "Please fill all the fields!", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun validInput(): Boolean {
        return binding.etUpdateStorageItemName.text.isNotEmpty() &&
                binding.etUpdateStorageItemAmount.text.isNotEmpty() &&
                binding.etUpdateStorageItemUnit.text.isNotEmpty()
    }

}