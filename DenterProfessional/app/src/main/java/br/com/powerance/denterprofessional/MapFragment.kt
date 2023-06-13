package br.com.powerance.denterprofessional

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.io.IOException


class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var auth: FirebaseAuth
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtenha uma referência ao SupportMapFragment e defina o retorno de chamada OnMapReadyCallback
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        auth = Firebase.auth
        val user = auth.currentUser
        val uid = user!!.uid

        // Salvar a referência ao mapa
        this.googleMap = googleMap

        puxarEnderecosDoFirestoreEAdicionarMarcadores(uid)
    }

    private fun puxarEnderecosDoFirestoreEAdicionarMarcadores(uid: String) {
        val db = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("users").document(uid)
        val context: Context = requireContext()
        collectionReference.get().addOnSuccessListener { documentSnapshot ->
            if(documentSnapshot.exists()) {
                val address1 = documentSnapshot.getString("address1")
                val address2 = documentSnapshot.getString("address2")
                val address3 = documentSnapshot.getString("address3")

                if (address1 != null) {
                    obterLatitudeLongitudePorEndereco(context, address1)?.let { latLng ->
                        googleMap?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("Endereço 1")
                        )
                    }
                }

                if (address2 != null) {
                    obterLatitudeLongitudePorEndereco(context, address2)?.let { latLng ->
                        googleMap?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("Endereço 2")
                        )
                    }
                }

                if (address3 != null) {
                    obterLatitudeLongitudePorEndereco(context, address3)?.let { latLng ->
                        googleMap?.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("Endereço 3")
                        )
                    }
                }
                // Mover a câmera do mapa para a posição dos marcadores
                val bounds = LatLngBounds.builder()
                if (address1 != null) obterLatitudeLongitudePorEndereco(context, address1)?.let {
                    bounds.include(
                        it
                    )
                }
                if (address2 != null) obterLatitudeLongitudePorEndereco(context, address2)?.let {
                    bounds.include(
                        it
                    )
                }
                if (address3 != null) obterLatitudeLongitudePorEndereco(context, address3)?.let {
                    bounds.include(
                        it
                    )
                }
                val padding = resources.getDimensionPixelSize(R.dimen.map_padding)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), padding))
            } else {
                // O documento com o UID fornecido não existe
            }
        }.addOnFailureListener { exception ->
            // Lidar com a falha na obtenção dos dados do Firestore
        }
    }

    fun obterLatitudeLongitudePorEndereco(context: Context, endereco: String): LatLng? {
        val geocoder = Geocoder(context)
        try {
            val addresses: List<Address> = geocoder.getFromLocationName(endereco, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                val lat = addresses[0].latitude
                val lng = addresses[0].longitude
                return LatLng(lat, lng)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}