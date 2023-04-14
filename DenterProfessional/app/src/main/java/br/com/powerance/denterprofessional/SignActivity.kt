package br.com.powerance.denterprofessional

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import br.com.powerance.denterprofessional.databinding.ActivitySignBinding


class SignActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}