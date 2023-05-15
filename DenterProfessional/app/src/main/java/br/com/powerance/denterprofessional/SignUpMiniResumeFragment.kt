package br.com.powerance.denterprofessional

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import br.com.powerance.denterprofessional.databinding.FragmentSignUpMiniResumeBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder

class SignUpMiniResumeFragment : Fragment() {

    private var _binding: FragmentSignUpMiniResumeBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions
    private val gson = GsonBuilder().enableComplexMapKeySerialization().create()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpMiniResumeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnEnd.setOnClickListener {
            val miniResume=binding.signUpMiniResume.text.toString()

            if(miniResume.isEmpty()){
                binding.signUpMiniResume.error="Insira seu mini curículo"
            }else{
                (activity as? SignActivity)?.let {
                    it.user.miniResume = miniResume
                }
                signUpNewAccount()
            }
        }
    }

    private fun signUpNewAccount(){
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword((activity as SignActivity).user.email,(activity as SignActivity).user.password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    (activity as SignActivity).storeUserId(user!!.uid)
                    //(activity as SignActivity).user.uid = (activity as SignActivity).getUserUid()
                    // atualizar o perfil do usuário com os dados chamando a function.
                    updateUserProfile()
                        .addOnCompleteListener(requireActivity()) { res ->
                            // conta criada com sucesso.
                            if(res.result.status == "SUCCESS"){
                                Snackbar.make(requireView(),"Conta cadastrada! Pode fazer o login!",
                                    Snackbar.LENGTH_LONG).show()
                                findNavController().navigate(R.id.action_SignUpMiniResume_to_SignIn)
                            }else{
//                                Snackbar.make(requireView(),res.result.payload.toString(),
//                                    Snackbar.LENGTH_LONG).show()
                                binding.signUpMiniResume.setText(res.result.message)
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                    // dar seguimento ao tratamento de erro.
                }
            }
    }
    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
    private fun updateUserProfile() : Task<CustomResponse> {
        // chamar a function para atualizar o perfil.
        functions = Firebase.functions("southamerica-east1")

        // Create the arguments to the callable function.
        val data = hashMapOf(
            "name" to (activity as? SignActivity)?.user?.name,
            "email" to (activity as? SignActivity)?.user?.email,
            "phone" to (activity as? SignActivity)?.user?.phone,
            "cep" to (activity as? SignActivity)?.user?.cep,
            "address1" to (activity as? SignActivity)?.user?.adress1,
            "address2" to (activity as? SignActivity)?.user?.adress2,
            "address3" to (activity as? SignActivity)?.user?.adress3,
            "miniResume" to (activity as? SignActivity)?.user?.miniResume,
            "status" to (activity as? SignActivity)?.user?.status,
            "fcmToken" to (activity as? SignActivity)?.user?.fcmToken,
            "uid" to (activity as? SignActivity)?.user?.uid
        )

        return functions
            .getHttpsCallable("setUserProfileUid")
            .call(data)
            .continueWith { task ->

                val result = gson.fromJson((task.result?.data as String), CustomResponse::class.java)
                result
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}