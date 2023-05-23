package br.com.powerance.denterprofessional

import android.content.Intent
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
        binding.button.setOnClickListener{
            val intent = Intent(activity, UserDetailProfileActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val menuActivity = requireActivity() as MenuActivity
        val dataProfile = menuActivity.dataProfile
        if(dataProfile.result.status == "ERROR"){
            Snackbar.make(requireView(),"Algo de estranho aconteceu! Tente novamente",
                        Snackbar.LENGTH_LONG).show()
        }else{
            var profile = gson.fromJson((dataProfile.result?.payload as String), Payload::class.java)
            binding.textView.text = profile.name
            binding.textView2.text = profile.email
            binding.switch1.setOnCheckedChangeListener { _, isChecked ->
                var status = false
                if (isChecked) {
                    status = true
                }
                updateUserProfile(status)
                    .addOnCompleteListener(requireActivity()) { res ->
                        // conta criada com sucesso.
                        if(res.result.status == "SUCCESS"){
                            var payload = gson.fromJson((res.result?.payload as String), Payload::class.java)
                            if(payload.status == true){
                                Snackbar.make(requireView(),"Você está ativo!",
                                    Snackbar.LENGTH_LONG).show()
                            }else{
                                Snackbar.make(requireView(),"Você está inativo!",
                                    Snackbar.LENGTH_LONG).show()
                            }
                        }else{
                            Snackbar.make(requireView(),res.result.message.toString(),
                                Snackbar.LENGTH_LONG).show()
                        }
                    }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun getUserProfile(): Task<CustomResponse> {
//        functions = Firebase.functions("southamerica-east1")
//
//        auth = Firebase.auth
//        val user = auth.currentUser
//        val uid = user!!.uid
//
//        val data = hashMapOf(
//            "uid" to uid
//        )
//
//        return functions
//            .getHttpsCallable("getUserProfileByUid")
//            .call(data)
//            .continueWith { task ->
//                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
//                result
//            }
//    }

    private fun updateUserProfile(status: Boolean?): Task<CustomResponse> {
        functions = Firebase.functions("southamerica-east1")

        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        val data = hashMapOf(
            "uid" to uid,
            "status" to status
        )

        return functions
            .getHttpsCallable("updateUserProfile")
            .call(data)
            .continueWith { task ->
                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }
    }

}