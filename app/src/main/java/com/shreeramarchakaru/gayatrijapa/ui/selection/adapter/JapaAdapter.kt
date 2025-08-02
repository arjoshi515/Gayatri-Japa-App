package com.shreeramarchakaru.gayatrijapa.ui.selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.data.models.Japa
import com.shreeramarchakaru.gayatrijapa.databinding.ItemJapaBinding

class JapaAdapter(
    private val onJapaClick: (Japa) -> Unit
) : ListAdapter<Japa, JapaAdapter.JapaViewHolder>(JapaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JapaViewHolder {
        val binding: ItemJapaBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_japa,
            parent,
            false
        )
        return JapaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JapaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class JapaViewHolder(
        private val binding: ItemJapaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(japa: Japa) {
            binding.japa = japa
            binding.executePendingBindings()

            // Update UI based on japa status
            when {
                japa.isCompleted -> {
                    binding.tvStatus.text = "Completed ✓"
                    binding.tvStatus.setBackgroundResource(R.drawable.bg_completed)
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                    binding.tvContinueHint.text = "Tap to restart or view history"
                }
                japa.isStarted -> {
                    binding.tvStatus.text = "In Progress"
                    binding.tvStatus.setBackgroundResource(R.drawable.bg_in_progress)
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
                    binding.tvContinueHint.text = "Tap to continue"
                }
                else -> {
                    binding.tvStatus.text = "Not Started"
                    binding.tvStatus.setBackgroundResource(R.drawable.bg_not_started)
                    binding.tvStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_secondary))
                    binding.tvContinueHint.text = "Tap to start"
                }
            }

            // Update progress bar color based on completion
            if (japa.isCompleted) {
                binding.progressBar.progressTintList = ContextCompat.getColorStateList(itemView.context, R.color.accent_color)
            } else {
                binding.progressBar.progressTintList = ContextCompat.getColorStateList(itemView.context, R.color.primary_color)
            }

            binding.root.setOnClickListener {
                onJapaClick(japa)
            }
        }
    }

    class JapaDiffCallback : DiffUtil.ItemCallback<Japa>() {
        override fun areItemsTheSame(oldItem: Japa, newItem: Japa): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Japa, newItem: Japa): Boolean {
            return oldItem == newItem
        }
    }
}