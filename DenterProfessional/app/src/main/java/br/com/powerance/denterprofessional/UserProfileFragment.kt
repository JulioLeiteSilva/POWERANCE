package br.com.powerance.denterprofessional

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.powerance.denterprofessional.databinding.FragmentUserProfileBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class UserProfileFragment: Fragment() {

    private var _binding: FragmentUserProfileBinding? = null

    private val binding get() = _binding!!

    private lateinit var functions: FirebaseFunctions

    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUsertoEmergencie.setOnClickListener {
            findNavController().navigate(R.id.action_User_to_Emergency)
        }

        getUserProfile()
            .addOnCompleteListener(requireActivity()){res ->
                if(res.result.status == "ERROR"){

                    Snackbar.make(requireView(),"Algo de estranho aconteceu! Tente novamente",
                        Snackbar.LENGTH_LONG).show()
                }else{
                    var profile = gson.fromJson((res.result?.payload as String), Payload::class.java)
                    binding.textView.text = profile.name
                    binding.textView2.text = profile.email
                }
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getUserProfile(): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        val data = hashMapOf(
            "uid" to uid
        )

        return functions
            .getHttpsCallable("getUserProfileByUid")
            .call(data)
            .continueWith { task ->
                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }



}