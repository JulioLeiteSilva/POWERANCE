package br.com.powerance.denterprofessional

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.powerance.denterprofessional.databinding.FragmentUserDetailProfileBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

// TO
class UserDetailProfileFragment : Fragment(), TextWatcher {

    private var _binding: FragmentUserDetailProfileBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private val binding get() = _binding!!

    private val menuActivity = requireActivity() as MenuActivity
    private val dataProfile = menuActivity.dataProfile

    var profile = gson.fromJson((dataProfile.result?.payload as String), Payload::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding.tiName.addTextChangedListener(this)
        binding.tiFone.addTextChangedListener(this)
        binding.tiResume.addTextChangedListener(this)
        binding.tiCep.addTextChangedListener(this)
        binding.tiAddress1.addTextChangedListener(this)
        binding.tiAddress2.addTextChangedListener(this)
        binding.tiAddress3.addTextChangedListener(this)

        _binding = FragmentUserDetailProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tiName.setText(profile.name)
        binding.tiFone.setText(profile.phone)
//        binding.tiEmail.setText(profile.email)
//        binding.tiPassword.setText(profile.pa)
        binding.tiResume.setText(profile.miniResume)
        binding.tiCep.setText(profile.cep)
        binding.tiAddress1.setText(profile.address1)
        binding.tiAddress2.setText(profile.address2)
        binding.tiAddress3.setText(profile.address3)
        if(profile.name == binding.tiName.text.toString() || profile.phone == binding.tiFone.text.toString() ||profile.miniResume == binding.tiResume.text.toString() ||profile.cep == binding.tiCep.text.toString() ||profile.address1 == binding.tiAddress1.text.toString() ||profile.address2 == binding.tiAddress2.text.toString() ||profile.address3 == binding.tiAddress3.text.toString()){
            binding.btnNext.isEnabled = false
        }
//        binding.btnNext.setOnClickListener{
//            findNavController().navigate(R.id.action_DetailProfile_to_user)
//        }


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
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Chamado durante a alteração do texto
    }
    override fun afterTextChanged(s: Editable?) {
        val novoTxt = s.toString()

        if(novoTxt == profile.name ||novoTxt == profile.phone ||novoTxt == profile.cep ||novoTxt == profile.miniResume ||novoTxt == profile.address1 ||novoTxt == profile.address2 ||novoTxt == profile.address3){
            binding.btnNext.isEnabled = false
        }
    }
}