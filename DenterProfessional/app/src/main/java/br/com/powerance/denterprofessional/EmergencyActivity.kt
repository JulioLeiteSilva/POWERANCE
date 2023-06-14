package br.com.powerance.denterprofessional

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EmergencyActivity : AppCompatActivity() {

    public var emergency: Emergency? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency)

        emergency = intent.getParcelableExtra("emergencia")

    }
}