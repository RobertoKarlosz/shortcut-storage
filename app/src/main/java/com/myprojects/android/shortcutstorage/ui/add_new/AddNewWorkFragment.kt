package hu.bme.aut.android.shortcutstorage.ui.add_new

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
import com.myprojects.android.shortcutstorage.databinding.FragmentAddNewWorkBinding
import hu.bme.aut.android.shortcutstorage.model.storage.StorageItem
import hu.bme.aut.android.shortcutstorage.model.work.Work
import hu.bme.aut.android.shortcutstorage.ui.storage.StorageViewModel
import hu.bme.aut.android.shortcutstorage.ui.work.WorkViewModel

class AddNewWorkFragment : Fragment() {

    private lateinit var binding : FragmentAddNewWorkBinding
    private lateinit var adapter : NewItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNewWorkBinding.inflate(inflater, container, false)

        val workViewModel : WorkViewModel = ViewModelProvider(requireActivity())[WorkViewModel::class.java]
        val storageViewModel : StorageViewModel = ViewModelProvider(requireActivity())[StorageViewModel::class.java]

        val storageItemsName : List<String> = storageViewModel.items.value?.map { it.name } ?: emptyList()

        val autocompleteAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            storageItemsName
            )

        binding.etAddWorkStorageItemName.setAdapter(autocompleteAdapter)

        val storageItems = ArrayList<StorageItem>()

        adapter = NewItemAdapter(object : NewItemAdapter.OptionsMenuClickListener {
            override fun onOptionsMenuClicked(position: Int) {
                val itemToDelete = storageItems[position]
                storageItems.remove(itemToDelete)
                adapter.update(storageItems)
            }
        })

        binding.rvAddMaterials.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAddMaterials.adapter = adapter

        binding.btnWorkAddItem.setOnClickListener {
            onWorkAddItemClicked(storageItems)
        }

        binding.btnAddWork.setOnClickListener {
            onAddWorkClicked(workViewModel, storageItems)
        }

        return binding.root
    }

    private fun onAddWorkClicked(
        workViewModel: WorkViewModel,
        storageItems: ArrayList<StorageItem>
    ) {
        if (validInput()) {
            workViewModel.add(
                Work(
                    id = 0,
                    name =
                    binding.etAddWorkName.text.toString()
                        .ifEmpty { binding.etAddWorkAddress.text.toString() },
                    address = binding.etAddWorkAddress.text.toString(),
                    date = binding.etAddWorkDate.text.toString(),
                    description = binding.etAddWorkDescription.text.toString(),
                    materials = storageItems
                )
            )
            Snackbar.make(requireView(), "Added Successfully!", Snackbar.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.navigation_work)
        } else {
            Snackbar.make(requireView(), "Please fill all required field!", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    private fun onWorkAddItemClicked(storageItems: ArrayList<StorageItem>) {
        if (validItem()) {
            val newItem = StorageItem(
                name = binding.etAddWorkStorageItemName.text.toString(),
                amount = binding.etAddWorkStorageItemAmount.text.toString().toInt(),
                unit = binding.etAddWorkStorageItemUnit.text.toString()
            )
            storageItems.add(newItem)
            adapter.update(storageItems)

            binding.etAddWorkStorageItemName.text.clear()
            binding.etAddWorkStorageItemAmount.text.clear()
            binding.etAddWorkStorageItemUnit.text.clear()

            binding.etAddWorkStorageItemName.requestFocus()
        } else {
            Snackbar.make(requireView(), "Fill all the items field", Snackbar.LENGTH_SHORT)
                .show()
            if (binding.etAddWorkStorageItemName.text.isEmpty()) {
                binding.etAddWorkStorageItemName.requestFocus()
            } else if (binding.etAddWorkStorageItemAmount.text.isEmpty()) {
                binding.etAddWorkStorageItemAmount.requestFocus()
            } else if (binding.etAddWorkStorageItemUnit.text.isEmpty()) {
                binding.etAddWorkStorageItemUnit.requestFocus()
            }
        }
    }

    private fun validInput(): Boolean {
        return binding.etAddWorkAddress.text.isNotEmpty() &&
                binding.etAddWorkDate.text.isNotEmpty()
    }

    private fun validItem(): Boolean {
        return binding.etAddWorkStorageItemName.text.isNotEmpty() &&
                binding.etAddWorkStorageItemAmount.text.isNotEmpty() &&
                binding.etAddWorkStorageItemUnit.text.isNotEmpty()
    }

}