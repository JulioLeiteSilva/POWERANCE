package br.com.powerance.denterprofessional

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.powerance.denterprofessional.databinding.ActivityEmergencyDetailBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class EmergencyDetailActivity : AppCompatActivity() {


    private lateinit var binding: ActivityEmergencyDetailBinding
    private var storage = Firebase.storage

    private var emergency: Emergency? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emergency = intent.getParcelableExtra("emergencia")

        binding.tvEmergencyName.text = getString(R.string.Nome_EmergencyDetail, emergency?.nome)
        binding.tvEmergencyPhone.text = getString(R.string.Telefone_EmergencyDetail, emergency?.telefone)


    }
}