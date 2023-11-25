package hu.bme.aut.android.shortcutstorage.ui.storage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.android.shortcutstorage.databinding.ItemStorageListBinding
import hu.bme.aut.android.shortcutstorage.model.storage.StorageItem

class StorageAdapter(
    private var optionsMenuClickListener: OptionsMenuClickListener
) : RecyclerView.Adapter<StorageAdapter.StorageViewHolder>() {
    inner class StorageViewHolder(val binding : ItemStorageListBinding) : RecyclerView.ViewHolder(binding.root)

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(position: Int)
    }

    private var items = emptyList<StorageItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageViewHolder {
        return StorageViewHolder(ItemStorageListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        val storageItem = items[position]
        holder.binding.tvStorageItemName.text = storageItem.name
        holder.binding.tvStorageItemAmount.text = storageItem.amount.toString()
        holder.binding.tvStorageItemUnit.text = storageItem.unit

        holder.binding.btnStorageItemOptions.setOnClickListener {
            optionsMenuClickListener.onOptionsMenuClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(storageItems : List<StorageItem>) {
        items = storageItems
        notifyDataSetChanged()
    }
}