package br.com.powerance.denterprofessional

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.powerance.denterprofessional.databinding.FragmentSignUpAddressBinding

class SignUpAddressFragment : Fragment() {

    private var _binding: FragmentSignUpAddressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignUpAddressBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext1.setOnClickListener{
            val cep = binding.signUpCEP.text.toString()
            val adress1 = binding.signUpAdress1.text.toString()
            val adress2 = binding.signUpAdress2.text.toString()
            val adress3 = binding.signUpAdress3.text.toString()

            if(cep.isEmpty()||adress1.isEmpty()){
                if(cep.isEmpty()){
                    binding.signUpCEP.error="Insira seu CEP"
                }
                if (adress1.isEmpty()){
                    binding.signUpAdress1.error="Insira seu Endereço principal"
                }
            }else if(cep.length!=9){
                binding.signUpCEP.error="CEP Inválido"
            }else{
                findNavController().navigate(R.id.action_SignUpAddress_to_SignUpMiniResume)
            }
        }

        binding.signUpCEP.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun afterTextChanged(s: Editable) {
            if (!s.toString().contains("-") && s.length > 5) {
                s.insert(5, "-");
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}