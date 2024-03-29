package br.com.powerance.denterprofessional

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.powerance.denterprofessional.databinding.FragmentEmergencyWaitingBinding

class EmergencyWaitingFragment : Fragment() {

    private var _binding: FragmentEmergencyWaitingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmergencyWaitingBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonForMap.setOnClickListener{
            findNavController().navigate(R.id.action_waiting_to_process)
        }
    }

    public fun teste(){
        findNavController().navigate(R.id.action_waiting_to_process)
    }
}