package hu.bme.aut.android.shortcutstorage.ui.storage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.myprojects.android.shortcutstorage.R
import com.myprojects.android.shortcutstorage.databinding.FragmentStorageBinding

class StorageFragment : Fragment() {

    private lateinit var binding : FragmentStorageBinding
    private lateinit var adapter : StorageAdapter
    private lateinit var viewModel: StorageViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStorageBinding.inflate(layoutInflater)

        adapter = StorageAdapter(object : StorageAdapter.OptionsMenuClickListener {
            override fun onOptionsMenuClicked(position: Int) {
                performOptionsMenuClicked(position)
            }
        })
        binding.rvStorage.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStorage.adapter = adapter

        binding.fabAddStorageItem.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_storage_to_addStorageItemFragment)
        }

        viewModel = ViewModelProvider(requireActivity())[StorageViewModel::class.java]
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter.update(items)
        }

        return binding.root
    }

    private fun performOptionsMenuClicked(position: Int) {
        val popupMenu = PopupMenu(requireContext(), binding.rvStorage[position].findViewById(R.id.btnStorageItemOptions))
        popupMenu.inflate(R.menu.pop_up_menu)
        popupMenu.setOnMenuItemClickListener ( object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.pop_up_update -> {
                        viewModel.updateItem = viewModel.items.value!![position]
                        findNavController().navigate(R.id.updateStorageItemFragment)
                        return true
                    }
                    R.id.pop_up_delete -> {
                        val storageItem = viewModel.items.value!![position]
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setPositiveButton("Yes") { _, _ ->
                            viewModel.deleteItem(storageItem)
                            Snackbar.make(requireView(), "Item deleted", Snackbar.LENGTH_SHORT)
                                .setAnchorView(binding.rvStorage)
                                .show()
                        }
                        builder.setNegativeButton("No") { _, _ ->}
                        builder.setTitle("Delete item")
                        builder.setMessage("Are you sure you want to delete ${storageItem.name}?")
                        builder.create().show()
                        return true
                    }
                }
                return false
            }

        })
        popupMenu.show()
    }
}