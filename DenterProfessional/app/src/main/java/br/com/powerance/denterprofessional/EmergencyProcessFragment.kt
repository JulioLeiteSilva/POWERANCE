package br.com.powerance.denterprofessional

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.powerance.denterprofessional.databinding.FragmentEmergencyProcessBinding
import com.google.firebase.auth.FirebaseAuth


class EmergencyProcessFragment : Fragment() {
    private var _binding: FragmentEmergencyProcessBinding? = null
    private lateinit var auth: FirebaseAuth
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
            irAteLocal()
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

    private fun irAteLocal() {
        // Lógica para ir até o local
    }

    private fun receberPaciente() {
        // Lógica para receber o paciente
    }
}
