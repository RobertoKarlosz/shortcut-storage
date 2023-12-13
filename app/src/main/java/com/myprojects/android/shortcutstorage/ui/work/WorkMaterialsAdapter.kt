package com.myprojects.android.shortcutstorage.ui.work

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.android.shortcutstorage.databinding.ItemWorkItemListBinding
import com.myprojects.android.shortcutstorage.model.storage.StorageItem

class WorkMaterialsAdapter(
    private var items : List<StorageItem>
) : RecyclerView.Adapter<WorkMaterialsAdapter.WorkItemsViewHolder>() {
    inner class WorkItemsViewHolder(val binding : ItemWorkItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkItemsViewHolder {
        return WorkItemsViewHolder(ItemWorkItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WorkItemsViewHolder, position: Int) {
        val storageItem = items[position]

        holder.binding.tvWorkItemName.text = storageItem.name
        holder.binding.tvWorkItemAmount.text = storageItem.amount.toString()
        holder.binding.tvWorkItemUnit.text = storageItem.unit
    }

    fun update(materials: List<StorageItem>){
        items = materials
    }
}