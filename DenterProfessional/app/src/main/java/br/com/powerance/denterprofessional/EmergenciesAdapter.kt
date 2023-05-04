package br.edu.puc.contactlist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.powerance.denterprofessional.Emergency
import br.com.powerance.denterprofessional.R
import com.bumptech.glide.Glide

class emergencyAdapter(private val dataSet: List<Emergency>) :
    ListAdapter<Emergency, emergencyAdapter.ContatoViewHolder>(ContatoDiffCallback) {

    class ContatoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val emergencyNameView: AppCompatTextView = itemView.findViewById(R.id.emergency_name_textview)
        private val emergencyNumberView: AppCompatTextView = itemView.findViewById(R.id.emergency_phone_textview)
        private val emergencyPhoto1View: ImageView = itemView.findViewById(R.id.emergency_photo1_imageview)
        private val emergencyPhoto2View: ImageView = itemView.findViewById(R.id.emergency_photo2_imageview)
        private val emergencyDateView: TextView = itemView.findViewById(R.id.emergency_date_textview)

        private var EmergencyAtual: Emergency? = null

        fun bind(t: Emergency) {
            EmergencyAtual = t
            emergencyNameView.text = t.nome
            emergencyNumberView.text = t.telefone
            emergencyDateView.text = t.dataHora
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_home, parent, false)
        return ContatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val t = dataSet[position]
        holder.bind(t)
    }

    override fun getItemCount() = dataSet.size
}
object ContatoDiffCallback : DiffUtil.ItemCallback<Emergency>() {
    override fun areItemsTheSame(oldItem: Emergency, newItem: Emergency): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: Emergency, newItem: Emergency): Boolean {
        return oldItem.nome == newItem.nome
    }
}