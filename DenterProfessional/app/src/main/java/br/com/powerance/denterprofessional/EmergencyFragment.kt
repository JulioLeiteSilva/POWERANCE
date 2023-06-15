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
import br.com.powerance.denterprofessional.datastore.CustomResponse
import br.com.powerance.denterprofessional.datastore.Emergency
import br.com.powerance.denterprofessional.datastore.Payload
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class EmergencyFragment : Fragment(), EmergencyAdapter.ClickEmergency {

    private var _binding: FragmentEmergencyBinding? = null
    private lateinit var functions: FirebaseFunctions
    private lateinit var auth: FirebaseAuth
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

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

//        db.collection("emergency").get()
//            .addOnSuccessListener {
//                if (!it.isEmpty){
//                    for (data in it.documents){
//                        val emergency: Emergency? = data.toObject(Emergency::class.java)
//                        if (emergency != null){
//                            if (emergency.status.equals("new")){
//                                emergency.uid = data.id
//                                emergencyList.add(emergency)
//                            }
//
//                        }
//                    }
//                }
//                if (recyclerView != null) {
//                    recyclerView.adapter = EmergencyAdapter(context,emergencyList, this)
//                }
//            }

        getEmergency()
            .addOnSuccessListener(requireActivity()){ res ->
                if(res.status == "SUCCESS"){
                    val emergency: Array<Emergency> = gson.fromJson((res.payload as String), Array<Emergency>::class.java)
                    if (recyclerView != null) {
                        recyclerView.adapter = EmergencyAdapter(context,ArrayList(emergency.toList()), this)
                    }
                    //Toast.makeText(requireActivity(), "SUCESSO", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireActivity(), "FALHOU", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun clickEmergency(emergency: Emergency) {

        val intent = Intent(context,EmergencyDetailActivity::class.java)
        intent.putExtra("emergencia",emergency)
        startActivity(intent)
    }

    private fun getEmergency(): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        val data = hashMapOf(
            "uid" to uid
        )

        return functions
            .getHttpsCallable("getEmergenciesByStatus")
            .call(data)
            .continueWith{task ->
                val result = gson.fromJson((task.result.data as String), CustomResponse::class.java)
                result
            }
    }
}
