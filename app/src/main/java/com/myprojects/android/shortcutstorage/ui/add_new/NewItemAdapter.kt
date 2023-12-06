package com.myprojects.android.shortcutstorage.ui.add_new

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.android.shortcutstorage.R
import com.myprojects.android.shortcutstorage.databinding.ItemStorageListBinding
import com.myprojects.android.shortcutstorage.model.storage.StorageItem

class NewItemAdapter(
    private var optionsMenuClickListener: OptionsMenuClickListener
) : RecyclerView.Adapter<NewItemAdapter.NewItemViewHolder>() {
    inner class NewItemViewHolder(val binding : ItemStorageListBinding) : RecyclerView.ViewHolder(binding.root)

    var items = mutableListOf<StorageItem>()

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewItemViewHolder {
        return NewItemViewHolder(ItemStorageListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: NewItemViewHolder, position: Int) {
        val storageItem = items[position]
        holder.binding.tvStorageItemName.text = storageItem.name
        holder.binding.tvStorageItemAmount.text = storageItem.amount.toString()
        holder.binding.tvStorageItemUnit.text = storageItem.unit

        holder.binding.btnStorageItemOptions.setImageResource(R.drawable.baseline_delete_24)

        holder.binding.btnStorageItemOptions.setOnClickListener {
            optionsMenuClickListener.onOptionsMenuClicked(position)
        }
    }

    fun update(updatedItems : List<StorageItem>) {
        items.clear()
        items.addAll(updatedItems)
        notifyDataSetChanged()
    }
}