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
import org.json.JSONObject

// TO
class UserDetailProfileFragment : Fragment() {

    private var _binding: FragmentUserDetailProfileBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    private val binding get() = _binding!!




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        _binding = FragmentUserDetailProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuActivity = requireActivity() as MenuActivity
        val dataProfile = menuActivity.dataProfile

        var profile = gson.fromJson((dataProfile.result?.payload as String), Payload::class.java)

        binding.tiName.setText(profile.name)
        binding.tiFone.setText(profile.phone)
        binding.tiResume.setText(profile.miniResume)
        binding.tiCep.setText(profile.cep)
        binding.tiAddress1.setText(profile.address1)
        binding.tiAddress2.setText(profile.address2.toString())
        binding.tiAddress3.setText(profile.address3.toString())


        binding.btnNext.setOnClickListener{
//            verificacao(binding.tiName.text.toString(),binding.tiFone.text.toString(),binding.tiResume.text.toString(),binding.tiCep.text.toString(),binding.tiAddress1.text.toString(),binding.tiAddress2.text.toString(),binding.tiAddress3.text.toString(), dataProfile)
//            findNavController().navigate(R.id.action_DetailProfile_to_user)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}