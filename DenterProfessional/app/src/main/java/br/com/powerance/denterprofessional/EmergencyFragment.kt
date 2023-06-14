package br.com.powerance.denterprofessional

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.com.powerance.denterprofessional.databinding.FragmentEmergencyBinding
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.internal.Util

class EmergencyFragment : Fragment(), EmergencyAdapter.ClickEmergency {

    private var _binding: FragmentEmergencyBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmergencyBinding.inflate(inflater,container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context: Context = requireContext()

        val emergencyList: ArrayList<Emergency> = arrayListOf()
        val recyclerView: RecyclerView? = view.findViewById(R.id.rvEmergencies)

        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

        db.collection("emergency").get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    for (data in it.documents){
                        val emergency: Emergency? = data.toObject(Emergency::class.java)
                        if (emergency != null){
                            if (emergency.status.equals("new")){
                                emergency.uid = data.id
                                emergencyList.add(emergency)
                            }

                        }
                    }
                }
                if (recyclerView != null) {
                    recyclerView.adapter = EmergencyAdapter(context,emergencyList, this)
                }
            }
    }

    override fun clickEmergency(emergency: Emergency) {

        val intent = Intent(context,EmergencyDetailActivity::class.java)
        intent.putExtra("emergencia",emergency)
        startActivity(intent)
    }

}
