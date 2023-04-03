package br.com.powerance.denterprofessional

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.powerance.denterprofessional.databinding.ActivitySignInBinding

lateinit var binding2: ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2 = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding2.root)

        binding2.tvLinkSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}