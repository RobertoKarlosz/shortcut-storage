package com.myprojects.android.shortcutstorage.ui.work

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.myprojects.android.shortcutstorage.R
import com.myprojects.android.shortcutstorage.databinding.FragmentWorkBinding
import com.myprojects.android.shortcutstorage.model.storage.StorageItem
import com.myprojects.android.shortcutstorage.ui.storage.StorageViewModel

class WorkFragment : Fragment() {

    private lateinit var binding : FragmentWorkBinding
    private lateinit var adapter : WorkAdapter
    private lateinit var workViewModel: WorkViewModel
    private lateinit var storageViewModel: StorageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkBinding.inflate(inflater, container, false)

        adapter = WorkAdapter(object : WorkAdapter.WorkButtonClickListener {
            override fun onOptionsMenuClicked(position: Int) {
                performOptionsMenuClicked(position)
            }

            override fun onDoneClicked(position: Int) {
                performDoneClicked(position)
            }
        })
        binding.rvWork.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWork.adapter = adapter

        binding.fabAddWork.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_work_to_addNewWorkFragment)
        }

        storageViewModel = ViewModelProvider(requireActivity())[StorageViewModel::class.java]

        workViewModel = ViewModelProvider(requireActivity())[WorkViewModel::class.java]
        workViewModel.works.observe(viewLifecycleOwner) { items ->
            adapter.update(items)
        }

        return binding.root
    }

    private fun performDoneClicked(position: Int) {
        val updateItem = workViewModel.works.value!![position]
        updateItem.done = !updateItem.done
        workViewModel.updateWork(updateItem)
        removeUsedMaterials(sumMaterials(updateItem.materials))
    }

    private fun performOptionsMenuClicked(position: Int) {
        val popupMenu = PopupMenu(requireContext(), binding.rvWork[position].findViewById(R.id.btnWorkOptions))
        popupMenu.inflate(R.menu.pop_up_menu)
        popupMenu.setOnMenuItemClickListener ( object : PopupMenu.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when(item?.itemId) {
                    R.id.pop_up_update -> {
                        workViewModel.updateWork = workViewModel.works.value!![position]
                        findNavController().navigate(R.id.action_navigation_work_to_updateWorkFragment)
                        return true
                    }
                    R.id.pop_up_delete -> {
                        val work = workViewModel.works.value!![position]
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setPositiveButton("Yes") { _, _ ->
                            workViewModel.deleteWork(work)
                            Snackbar.make(requireView(), "Work deleted", Snackbar.LENGTH_SHORT)
                                .setAnchorView(binding.rvWork)
                                .show()
                        }
                        builder.setNegativeButton("No") { _, _ ->}
                        builder.setTitle("Delete work")
                        builder.setMessage("Are you sure you want to delete ${work.name}?")
                        builder.create().show()
                        return true
                    }
                }
                return false
            }

        })
        popupMenu.show()
    }

    private fun sumMaterials(materials : List<StorageItem>) : List<StorageItem> {
        val sumMaterial = ArrayList<StorageItem>()
        for(material in materials) {
            val sumMaterialsNames = sumMaterial.map { it.name }
            if(sumMaterialsNames.contains(material.name)) {
                val index = sumMaterialsNames.indexOf(material.name)
                sumMaterial[index].amount += material.amount
            }
            else {
                sumMaterial.add(material)
            }
        }
        return sumMaterial
    }

    private fun removeUsedMaterials(materials: List<StorageItem>) {
        val storageItemNames = storageViewModel.items.value?.map { it.name } ?: emptyList()
        val materialsNames = materials.map { it.name }
        for(material in materialsNames) {
            if(storageItemNames.contains(material)) {
                val materialIndex: Int = material.indexOf(material)
                val storageIndex: Int = storageItemNames.indexOf(material)
                val removeItem: StorageItem = storageViewModel.items.value!![storageIndex]
                removeItem.amount -= materials[materialIndex].amount
                storageViewModel.updateItem(removeItem)
            }
        }
    }
}
