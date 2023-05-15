package br.com.powerance.denterprofessional


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class EmergencyAdapter(private val dataSet: List<Emergency>) :
    ListAdapter<Emergency, EmergencyAdapter.EmergencyViewHolder>(EmergencyDiffCallback) {

    class EmergencyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val EmergencyName: TextView = itemView.findViewById(R.id.tvEmergencyName)
        private val EmergencyDescription: TextView = itemView.findViewById(R.id.tvEmergencyDescription)
        private var EmergencyAtual: Emergency? = null
        
        fun bind(t: Emergency) {
            EmergencyAtual = t
            EmergencyName.text = t.nome
            EmergencyDescription.text = t.descricao
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.emergency_item, parent, false)
        return EmergencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        val t = dataSet[position]
        holder.bind(t)
    }

    override fun getItemCount() = dataSet.size
}
object EmergencyDiffCallback : DiffUtil.ItemCallback<Emergency>() {
    override fun areItemsTheSame(oldItem: Emergency, newItem: Emergency): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: Emergency, newItem: Emergency): Boolean {
        return oldItem.uid == newItem.uid
    }
}