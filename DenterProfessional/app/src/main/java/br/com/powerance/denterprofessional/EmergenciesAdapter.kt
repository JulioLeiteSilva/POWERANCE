package br.com.powerance.denterprofessional


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.powerance.denterprofessional.datastore.Emergency

class EmergencyAdapter(val context: Context, var emergencies: ArrayList<Emergency>, var clickEmergency: ClickEmergency):
        RecyclerView.Adapter<EmergencyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.emergency_item,parent, false)

        val holder = ViewHolder(view)

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val emergency: Emergency = emergencies[position]
//        val documentID = documentsIdsList[position]

        holder.nome.text = emergency.name

        holder.itemEmergencia.setOnClickListener{

            clickEmergency.clickEmergency(emergency)

        }

    }

    override fun getItemCount(): Int {
        return emergencies.size
    }

    interface ClickEmergency{
        fun clickEmergency(emergency: Emergency)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nome: TextView = itemView.findViewById(R.id.tvEmergencyName)
        val itemEmergencia: LinearLayout = itemView.findViewById(R.id.itemEmergency)

    }


}