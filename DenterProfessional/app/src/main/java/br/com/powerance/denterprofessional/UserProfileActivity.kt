package br.com.powerance.denterprofessional

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.powerance.denterprofessional.databinding.ActivitySignBinding


class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}