package br.com.powerance.denterprofessional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.powerance.denterprofessional.databinding.FragmentSignUpMiniResumeBinding

class SignUpMiniResumeFragment : Fragment() {
    private var _binding: FragmentSignUpMiniResumeBinding? = null

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
                //Aqui é chamada a function do firebase
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}