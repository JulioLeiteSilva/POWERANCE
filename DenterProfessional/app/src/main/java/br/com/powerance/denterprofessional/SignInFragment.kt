package br.com.powerance.denterprofessional

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import br.com.powerance.denterprofessional.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private lateinit var auth: FirebaseAuth;
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLinkSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_SignIn_to_SignUp)
        }
        binding.btnEntrar.setOnClickListener{

            var email : String = binding.signInEmail.text.toString()
            var password : String = binding.signInPassword.text.toString()

            binding.signInPasswordLayout.isPasswordVisibilityToggleEnabled = true

            if(email.isEmpty()||password.isEmpty()){
                if(email.isEmpty()){
                    binding.signInEmail.error="Insira seu endereço email"
                }
                if(password.isEmpty()){
                    binding.signInPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    binding.signInPassword.error="Insira sua senha"
                }
            }else if(!email.matches(emailPattern.toRegex())){
                binding.signInProgressBar.visibility= View.GONE
                binding.signInEmail.error="Insira seu endereço email valido"
                Toast.makeText(activity,"Insira um endereço de email valido", Toast.LENGTH_SHORT).show()
            }else if(password.length < 6){
                binding.signInProgressBar.visibility= View.GONE
                binding.signInPassword.error="A senha deve ter mais de 6 digitos"
                Toast.makeText(activity,"A senha deve ter mais de 6 digitos", Toast.LENGTH_SHORT).show()
            }else{
                login(email,password)
            }

        }
    }
//    private fun View.hideKeyboard() {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(windowToken, 0)
//    }
    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
    private fun login(email: String, password: String){
        hideKeyboard()
        // inicializando o auth.
        auth = Firebase.auth

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // login completado com sucesso.
                    //findNavController().navigate(R.id.action_login_to_info)
                    val intent = Intent(activity, MenuActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    if (it.exception is FirebaseAuthException) {
                        Snackbar.make(requireView(),"Não foi possível fazer o login, verifique os dados e tente novamente.", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}