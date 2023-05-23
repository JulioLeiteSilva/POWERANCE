package br.com.powerance.denterprofessional


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class EmergencyAdapter(private val dataSet: List<Emergency>) :
    ListAdapter<Emergency, EmergencyAdapter.EmergencyViewHolder>(EmergencyDiffCallback) {

    class EmergencyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val EmergencyName: TextView = itemView.findViewById(R.id.tvEmergencyName)
        private var EmergencyAtual: Emergency? = null
        private var EmergencyButton: Button = itemView.findViewById(R.id.botao111)
        
        fun bind(t: Emergency) {
            EmergencyAtual = t
            EmergencyName.text = t.nome
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.emergency_item, parent, false)
        return EmergencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        val t = dataSet[position]
        

        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(holder.itemView.context, EmergencyDetailActivity::class.java)

            intent.putExtra("emergencyId", t.uidSocorrista)
            intent.putExtra("emergencyName", t.nome)

            holder.itemView.context.startActivity(intent)
            Toast.makeText(holder.itemView.context, "Authentication failed.",
                Toast.LENGTH_SHORT).show()

        })

        holder.bind(t)
    }

    override fun getItemCount() = dataSet.size
}
object EmergencyDiffCallback : DiffUtil.ItemCallback<Emergency>() {
    override fun areItemsTheSame(oldItem: Emergency, newItem: Emergency): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: Emergency, newItem: Emergency): Boolean {
        return oldItem.uidSocorrista == newItem.uidSocorrista
    }
}