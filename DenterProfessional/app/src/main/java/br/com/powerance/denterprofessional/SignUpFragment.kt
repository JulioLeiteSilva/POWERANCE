package br.com.powerance.denterprofessional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import br.com.powerance.denterprofessional.databinding.FragmentSignUpBinding


private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val name = binding.signUpName.text.toString()
            val email = binding.signUpEmail.text.toString()
            val cEmail = binding.signUpCEmail.text.toString()
            val phone = binding.signUpFone.text.toString()
            val password = binding.signUpPassword.text.toString()
            val cPassword = binding.signUpCPassword.text.toString()
            val adress1 = ""
            val adress2 = ""
            val adress3 = ""
            val miniResume=""

            binding.signUPProgressBar.visibility= View.VISIBLE
            binding.signUpPasswordLayout.isPasswordVisibilityToggleEnabled = true
            binding.signUpCPasswordLayout.isPasswordVisibilityToggleEnabled = true

            if(name.isEmpty()||email.isEmpty()||cEmail.isEmpty()||phone.isEmpty()||password.isEmpty()||cPassword.isEmpty()){
                if(name.isEmpty()){
                    binding.signUpName.error="Insira seu nome"
                }
                if(email.isEmpty()){
                    binding.signUpEmail.error="Insira seu endereço email"
                }
                if(cEmail.isEmpty()){
                    binding.signUpCEmail.error="Repita seu endereço de email"
                }
                if(phone.isEmpty()){
                    binding.signUpFone.error="Insira seu número de telefone"
                }
                if(password.isEmpty()){
                    binding.signUpPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    binding.signUpPassword.error="Insira sua senha"
                }
                if(cPassword.isEmpty()){
                    binding.signUpCPasswordLayout.isPasswordVisibilityToggleEnabled = false
                    binding.signUpCPassword.error="Repita sua senha"
                }
                Toast.makeText(activity,"Insira detalhes validos", Toast.LENGTH_SHORT).show()
                binding.signUPProgressBar.visibility= View.GONE
            }else if(!email.matches(emailPattern.toRegex())){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpEmail.error="Insira seu endereço email valido"
                Toast.makeText(activity,"Insira um endereço de email valido", Toast.LENGTH_SHORT).show()
            }else if(phone.length!=10){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpFone.error="Insira um número de telefone valido"
                Toast.makeText(activity,"Insira um número de telefone valido", Toast.LENGTH_SHORT).show()
            }else if(password.length < 6){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpPassword.error="A senha deve ter mais de 6 digitos"
                Toast.makeText(activity,"A senha deve ter mais de 6 digitos", Toast.LENGTH_SHORT).show()
            }else if(password != cPassword){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpCPassword.error="As senhas não batem"
                Toast.makeText(activity,"As senhas não batem", Toast.LENGTH_SHORT).show()
            }else if(email != cEmail){
                binding.signUPProgressBar.visibility= View.GONE
                binding.signUpCEmail.error="Os Emails não batem"
                Toast.makeText(activity,"Os Emails não batem", Toast.LENGTH_SHORT).show()
            }else{
                findNavController().navigate(R.id.action_SignUp_to_SignUpAddress)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}