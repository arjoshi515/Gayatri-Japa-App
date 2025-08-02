// ui/history/HistoryAdapter.kt
package com.shreeramarchakaru.gayatrijapa.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val onItemClick: (Japa) -> Unit
) : ListAdapter<Japa, HistoryAdapter.HistoryViewHolder>(JapaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HistoryViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(japa: Japa) {
            binding.japa = japa
            binding.executePendingBindings()
        }
    }

    private class JapaDiffCallback : DiffUtil.ItemCallback<Japa>() {
        override fun areItemsTheSame(oldItem: Japa, newItem: Japa): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Japa, newItem: Japa): Boolean {
            return oldItem == newItem
        }
    }
}