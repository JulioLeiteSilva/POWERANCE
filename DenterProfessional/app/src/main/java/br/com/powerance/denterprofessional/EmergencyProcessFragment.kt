package br.com.powerance.denterprofessional

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.powerance.denterprofessional.databinding.FragmentEmergencyProcessBinding


class EmergencyProcessFragment : Fragment() {
    private var _binding: FragmentEmergencyProcessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyProcessBinding.inflate(inflater, container, false)
        val view = binding.root

        val emergencyData = (activity as? EmergencyActivity)?.emergency

        binding.buttonTelefonar.setOnClickListener {
            if (emergencyData != null) {
                fazerChamada(emergencyData.phone)
            }
        }

        binding.buttonIrAteLocal.setOnClickListener {

            if (emergencyData != null) {
//                irAteLocal(emergencyData)
            }
        }

        binding.buttonReceberPaciente.setOnClickListener {
            receberPaciente()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fazerChamada(telefone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telefone"))
        startActivity(intent)
    }

//    private fun irAteLocal(emergency: Emergency) {
//        val latitude = emergency.location.latitude
//        val longitude = emergency.location.longitude
//
//        val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//
//        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
//            startActivity(mapIntent)
//        } else {
//            // Lide com a situação em que não há aplicativo de mapas instalado
//        }
//    }


    private fun receberPaciente() {
        // Lógica para receber o paciente
    }
}
