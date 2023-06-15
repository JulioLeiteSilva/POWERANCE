package br.com.powerance.denterprofessional

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.powerance.denterprofessional.databinding.FragmentEmergencyProcessBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint


class EmergencyProcessFragment : Fragment() {
    private var _binding: FragmentEmergencyProcessBinding? = null
    private lateinit var auth: FirebaseAuth
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyProcessBinding.inflate(inflater, container, false)
        val view = binding.root

        val emergencyData = (activity as? EmergencyActivity)?.emergency
        val userAdress = (activity as? EmergencyActivity)?.userAddress

        binding.buttonTelefonar.setOnClickListener {
            if (emergencyData != null) {
                fazerChamada(emergencyData.phone)
            }
        }

        binding.buttonIrAteLocal.setOnClickListener {

            if (emergencyData != null) {
                val documentId = emergencyData.uid
                obterLocalizacaoPorId(documentId) { geoPoint ->
                    if (geoPoint != null) {
                        val latitude = geoPoint.latitude
                        val longitude = geoPoint.longitude
                        if (userAdress != null) {
                            irAteLocal(userAdress,latitude,longitude)
                        }
                    } else {
                        // Documento não encontrado ou ocorreu um erro
                        // Lide com essa situação adequadamente
                    }
                }
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

    fun obterLocalizacaoPorId(documentId: String, callback: (GeoPoint?) -> Unit) {
        val docRef = db.collection("emergency").document(documentId)
        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        val geoPoint = document.getGeoPoint("location")
                        callback(geoPoint)
                    } else {
                        // Documento não encontrado
                        callback(null)
                    }
                } else {
                    // Erro na busca do documento
                    callback(null)
                }
            }
    }
    // A
    private fun irAteLocal(enderecoPartida: String, latitudeDestino: Double, longitudeDestino: Double) {

        val geocoder = context?.let { Geocoder(it) }
        val addressList = geocoder?.getFromLocationName(enderecoPartida, 1) // Pegar localização do endereço do dentista
        if (addressList != null) {
            if (addressList.isNotEmpty()) {
                val address = addressList[0]
                val partidaLatitude = address.latitude
                val partidaLongitude = address.longitude

                // Mapa Vai com Localização atual do Dentista
                val gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$latitudeDestino,$longitudeDestino&travelmode=driving")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    // Lide com a situação em que não há aplicativo de mapas instalado
                }
            } else {
                // Lide com a situação em que o endereço de partida não pôde ser convertido em coordenadas geográficas
            }
        }
    }

    private fun receberPaciente() {
        // Lógica para receber o paciente
    }
}
