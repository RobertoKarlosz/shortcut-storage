package com.myprojects.android.shortcutstorage.ui.work

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myprojects.android.shortcutstorage.databinding.ItemWorkListBinding
import com.myprojects.android.shortcutstorage.model.work.Work

class WorkAdapter(
    private var workButtonClickListener: WorkButtonClickListener
) : RecyclerView.Adapter<WorkAdapter.WorkViewHolder>() {
    inner class WorkViewHolder(val binding: ItemWorkListBinding) : RecyclerView.ViewHolder(binding.root)

    interface WorkButtonClickListener {
        fun onOptionsMenuClicked(position: Int)

        fun onDoneClicked(position: Int)
    }

    private var works = emptyList<Work>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkViewHolder {
        return WorkViewHolder(ItemWorkListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WorkViewHolder, position: Int) {
        val work = works[position]

        holder.binding.tvWorkAddress.text = work.address
        holder.binding.tvWorkDate.text = work.date
        holder.binding.tvWorkName.text = work.name
        holder.binding.tvWorkDescription.text =
            work.description.ifEmpty { "" }
        holder.binding.tvWorkMaterials.text =
            if (work.materials.isEmpty()) ""
            else "Materials"
        val adapter = WorkMaterialsAdapter(work.materials)
        holder.binding.rvWorkItems.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.binding.rvWorkItems.adapter = adapter

        holder.binding.btnWorkOptions.setOnClickListener {
            workButtonClickListener.onOptionsMenuClicked(position)
        }

        holder.binding.cbDone.isChecked = work.done
        holder.binding.cbDone.isEnabled = !work.done
        holder.binding.cbDone.setOnClickListener {
            holder.binding.cbDone.isEnabled = false
            workButtonClickListener.onDoneClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return works.size
    }

    fun update(updatedWorks : List<Work>) {
        works = updatedWorks
        notifyDataSetChanged()
    }
}