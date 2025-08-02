package com.shreeramarchakaru.gayatrijapa.utils.common


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shreeramarchakaru.gayatrijapa.R
import com.shreeramarchakaru.gayatrijapa.utils.LanguageManager

class LanguageAdapter(
    private val languages: List<LanguageManager.Language>,
    private val currentLanguage: String,
    private val onLanguageSelected: (String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_language, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(languages[position])
    }

    override fun getItemCount(): Int = languages.size

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvLanguageName: TextView = itemView.findViewById(R.id.tvLanguageName)
        private val rbLanguage: RadioButton = itemView.findViewById(R.id.rbLanguage)

        fun bind(language: LanguageManager.Language) {
            tvLanguageName.text = language.displayName
            rbLanguage.isChecked = language.code == currentLanguage

            itemView.setOnClickListener {
                onLanguageSelected(language.code)
            }

            rbLanguage.setOnClickListener {
                onLanguageSelected(language.code)
            }
        }
    }
}